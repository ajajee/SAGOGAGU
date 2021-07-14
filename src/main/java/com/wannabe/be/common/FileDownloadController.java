package com.wannabe.be.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import net.coobird.thumbnailator.Thumbnails;

@Controller
public class FileDownloadController {
	private static String CURR_IMAGE_REPO_PATH = "C:\\upload";

	@RequestMapping("/download/{uploadpath1:.+}/{uploadpath2:.+}/{uploadpath3:.+}/{uuid:.+}/{filename:.+}")
	protected void download(
			@PathVariable String uploadpath1,
			@PathVariable String uploadpath2,
			@PathVariable String uploadpath3,
			@PathVariable String uuid,
			@PathVariable String filename,
			HttpServletResponse response) throws Exception {
		System.out.println(uploadpath1+uploadpath2+uploadpath3+uuid+filename);
		OutputStream out = response.getOutputStream();
		
		String filePath = CURR_IMAGE_REPO_PATH + "\\" + uploadpath1+ "\\" + uploadpath2 + "\\" + uploadpath3+ "\\" + "s_" + uuid + "_" + filename;
		File image = new File(filePath);

		response.setHeader("Cache-Control", "no-cache");
		response.addHeader("Content-disposition", "attachment; fileName=" + filename);
		FileInputStream in = new FileInputStream(image);
		byte[] buffer = new byte[1024 * 8];
		while (true) {
			int count = in.read(buffer); // ���ۿ� �о���� ���ڰ���
			if (count == -1) // ������ �������� �����ߴ��� üũ
				break;
			out.write(buffer, 0, count);
		}
		in.close();
		out.close();
	}

	@RequestMapping("/thumbnails.do")
	protected void thumbnails(@RequestParam("uploadpath") String uploadpath, @RequestParam("filename") String filename,
			HttpServletResponse response) throws Exception {
		OutputStream out = response.getOutputStream();
		String filePath = CURR_IMAGE_REPO_PATH + "\\" + uploadpath + "\\" + filename;
		File image = new File(filePath);

		if (image.exists()) {
			Thumbnails.of(image).size(121, 154).outputFormat("png").toOutputStream(out);
		}
		byte[] buffer = new byte[1024 * 8];
		out.write(buffer);
		out.close();
	}
}
