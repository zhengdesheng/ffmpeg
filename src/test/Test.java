package test;

import vedio.ffmpeg.ConvertVedio;

public class Test {

	public static void main(String[] args) {
		String path = "D:\\OTTRaid\\content\\vod\\ffmpeg\\";
		String inputPath = path + "test.wmv";
		ConvertVedio.convertVedio(inputPath);

	}

}
