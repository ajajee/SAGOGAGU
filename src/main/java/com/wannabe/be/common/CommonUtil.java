package com.wannabe.be.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.wannabe.be.product.vo.ProductAttachVO;

import net.coobird.thumbnailator.Thumbnailator;

public class CommonUtil {
	// 이미지 저장 및 uuid, 파일이름, 타입 설정
	public static List<ProductAttachVO> upload(MultipartHttpServletRequest multipartRequest) {

		String imageFileName = null;
		Iterator<String> fileNames = multipartRequest.getFileNames();

		List<ProductAttachVO> list = new ArrayList<>();
		String uploadFolder = "C:\\upload";

		String uploadFolderPath = getFolder();
		// make folder --------
		File uploadPath = new File(uploadFolder, uploadFolderPath);
		if (!uploadPath.exists()) {
			uploadPath.mkdirs();
			System.out.println("HI");
		}
		// make yyyy/MM/dd folder

		while (fileNames.hasNext()) {

			ProductAttachVO productAttachVO = new ProductAttachVO();
			String fileName = fileNames.next(); // input file name 뽑기
			// productAttachVO.setFiletype(fileName); // 메인 이미지, 상세 이미지 타입 구분
			System.out.println(fileName);
			MultipartFile mFile = multipartRequest.getFile(fileName); // input file name으로 파일 뽑기
			String uploadFileName = mFile.getOriginalFilename(); // 파일에서 이미지 이름 뽑기
			uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf("\\") + 1); //
			productAttachVO.setFilename(uploadFileName);
			UUID uuid = UUID.randomUUID();
			uploadFileName = uuid.toString() + "_" + uploadFileName;

			try {
				File saveFile = new File(uploadPath, uploadFileName);
				// productAttachVO.setUuid(uuid.toString());
				productAttachVO.setUploadpath(uploadFolderPath);

				if (checkImageType(saveFile)) {
					System.out.println(uploadPath);
					FileOutputStream thumbnail = new FileOutputStream(new File(uploadPath, "s_" + uploadFileName));
					System.out.println(uploadFileName);
					Thumbnailator.createThumbnail(mFile.getInputStream(), thumbnail, 100, 100);
					mFile.transferTo(saveFile);

					System.out.println("111");
					thumbnail.close();
					System.out.println("222");
				}

				// add to List
				list.add(productAttachVO);
				System.out.println(list);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return list;
	}

	// 이미지 저장경로 폴더 만들기
	private static String getFolder() {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Date date = new Date();

		String str = sdf.format(date);

		return str.replace("-", File.separator);
	}

	// 이미지 파일 확인
	private static boolean checkImageType(File file) {

		try {
			String contentType = Files.probeContentType(file.toPath());

			return contentType.startsWith("image");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}
}
