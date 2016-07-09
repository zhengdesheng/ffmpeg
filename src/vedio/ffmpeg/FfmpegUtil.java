package vedio.ffmpeg;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 将视频格式转换为ts流
 * 
 * @author Administrator
 *
 */
public class FfmpegUtil {

	/**
	 * 将视频转换为ts流
	 * 
	 * @param ffmpegPath
	 *            ffmpegPath bin路径
	 * @param inputPath
	 *            源文件路径
	 * @param outputPath
	 *            输出文件路径
	 * @return
	 */
	public static Boolean ffmpeg(String ffmpegPath, String inputPath, String outputPath) throws FFmpegException{
 
		if (!checkfile(inputPath)) {
			 throw new FFmpegException("文件格式不合法");
		}

		int type = checkContentType(inputPath);
		List<String> command = getFfmpegCommand(type, ffmpegPath, inputPath, outputPath);
		if (null != command && command.size() > 0) {
			return process(command);
			 
		}
		return false;
	}

	/**
	 * 检查视频的格式
	 * 
	 * @param inputPath
	 *            源文件
	 * @return
	 */
	private static int checkContentType(String inputPath) {
		String type = inputPath.substring(inputPath.lastIndexOf(".") + 1, inputPath.length()).toLowerCase();
		// ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）
		if (type.equals("avi")) {
			return 1;
		} else if (type.equals("mpg")) {
			return 1;
		} else if (type.equals("wmv")) {
			return 1;
		} else if (type.equals("3gp")) {
			return 1;
		} else if (type.equals("mov")) {
			return 1;
		} else if (type.equals("mp4")) {
			return 1;
		} else if (type.equals("mkv")){
			return 1;
		}else if (type.equals("asf")) {
			return 0;
		} else if (type.equals("flv")) {
			return 0;
		}else if (type.equals("rm")) {
			return 0;
		} else if (type.equals("rmvb")) {
			return 1;
		}
		return 9;
	}

	/**
	 * 检查文件的合法性
	 * 
	 * @param path
	 * @return
	 */
	private static boolean checkfile(String path) {
		File file = new File(path);
		if (!file.isFile()) {
			return false;
		}
		return true;
	}

	/**
	 * ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）
	 * 
	 * @param command
	 * @param dto
	 * @throws FFmpegException 
	 */
	private static boolean process(List<String> command) throws FFmpegException{

		try {

			if (null == command || command.size() == 0)
				return false;
			Process videoProcess = new ProcessBuilder(command).redirectErrorStream(true).start();

			new PrintStream(videoProcess.getErrorStream()).start();

			new PrintStream(videoProcess.getInputStream()).start();

			int exitcode = videoProcess.waitFor();
			 
			if (exitcode == 1) {
				return false;
			}
			return true;
		} catch (Exception e) {
			throw new FFmpegException("file upload failed",e);
		}

	}

	/**
	 * 根据文件类型设置ffmpeg命令
	 * 
	 * @param type
	 * @param oldfilepath
	 * @param outputPath
	 * @param dto
	 * @return
	 * @throws FFmpegException 
	 */
	private static List<String> getFfmpegCommand(int type, String ffmpegPath, String oldfilepath, String outputPath) throws FFmpegException {
		List<String> command = new ArrayList<String>();
		if (type == 1) {
			command.add(ffmpegPath + "\\ffmpeg");
			command.add("-i");
			command.add(oldfilepath);
			command.add("-c:v");
			command.add("libx264");
			command.add("-x264opts");
			command.add("force-cfr=1");
			command.add("-c:a");
			command.add("mp2");
			command.add("-b:a");
			command.add("256k");
			command.add("-vsync");
			command.add("cfr");
			command.add("-f");
			command.add("mpegts");
			command.add(outputPath);
		} else if(type==0){
			command.add(ffmpegPath + "\\ffmpeg");
			command.add("-i");
			command.add(oldfilepath);
			command.add("-c:v");
			command.add("libx264");
			command.add("-x264opts");
			command.add("force-cfr=1");
			command.add("-vsync");
			command.add("cfr");
			command.add("-vf");
			command.add("idet,yadif=deint=interlaced");
			command.add("-filter_complex");
			command.add("aresample=async=1000");
			command.add("-c:a");
			command.add("libmp3lame");
			command.add("-b:a");
			command.add("192k");
			command.add("-pix_fmt");
			command.add("yuv420p");
			command.add("-f");
			command.add("mpegts");
			command.add(outputPath);
		}else{
			throw new FFmpegException("不支持当前上传的文件格式");
		}
		return command;
	}
}

class PrintStream extends Thread {
	java.io.InputStream __is = null;

	public PrintStream(java.io.InputStream is) {
		__is = is;
	}

	public void run() {
		try {
			while (this != null) {
				int _ch = __is.read();
				if (_ch == -1) {
					break;
				} else {
					System.out.print((char) _ch);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
