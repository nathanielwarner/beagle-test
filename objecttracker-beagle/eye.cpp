#define HUE 2
#define HUE_TOLERANCE 80
#define SATURATION 200
#define SATURATION_TOLERANCE 100
#define BRIGHTNESS 200
#define BRIGHTNESS_TOLERANCE 100

#include <iostream>
#include <csignal>
#include <opencv2/videoio/videoio.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include "coloredobject.hpp"

using namespace std;
using namespace cv;

bool interruptSig = false;

void signalHandler( int signum )
{
    cout << "Interrupt signal (" << signum << ") received.\n";
   	interruptSig = true;
}

int main(int argc, const char** argv)
{
	signal(SIGINT, signalHandler);
	
	VideoCapture cap(0);
	
	if (!cap.isOpened())
	{
		cout << "Cannot open the camera" << endl;
		return -1;
	}
	
	double dWidth = cap.get(CAP_PROP_FRAME_WIDTH);
	double dHeight = cap.get(CAP_PROP_FRAME_HEIGHT);
	
	cout << "Frame size: " << dWidth << " x " << dHeight << endl;
	ofstream file;
	file.open("cam_size");
	file << dWidth << " " << dHeight << endl;
	file.close();
	
	Mat frameBGR, frameHSV;
	bool bSuccess = cap.read(frameBGR);
	
	if (!bSuccess)
	{
		cout << "Cannot read from the video stream" << endl;
		return -1;
	}
	
	GaussianBlur(frameBGR, frameBGR, Size(3, 3), 0, 0);
	cvtColor(frameBGR, frameHSV, CV_BGR2HSV);
	
	ColoredObject object(0, HUE, SATURATION, BRIGHTNESS, &frameHSV, HUE_TOLERANCE, SATURATION_TOLERANCE, BRIGHTNESS_TOLERANCE);
	
	while (!interruptSig)
	{
		bool bSuccess = cap.read(frameBGR);
		
		if (!bSuccess)
		{
			cout << "Cannot read from the video stream" << endl;
			return -1;
		}
		
		GaussianBlur(frameBGR, frameBGR, Size(3, 3), 0, 0);
		cvtColor(frameBGR, frameHSV, CV_BGR2HSV);
		
		object.tick();
		
	}
	
	object.terminate();
	system("rm cam_size");
	return 0;
}
