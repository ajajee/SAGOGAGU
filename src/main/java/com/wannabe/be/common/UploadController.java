//package com.wannabe.be.common;
//
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.UUID;
//
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.util.FileCopyUtils;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.wannabe.be.product.vo.AttachFileDTO;
//
//import net.coobird.thumbnailator.Thumbnailator;
//
//
//@Controller
//public class UploadController {
//	private String getFolder() {
//
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//
//		Date date = new Date();
//
//		String str = sdf.format(date);
//
//		return str.replace("-", File.separator);
//	}
//	
//	private boolean checkImageType(File file) {
//
//		try {
//			String contentType = Files.probeContentType(file.toPath());
//
//			return contentType.startsWith("image");
//
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		return false;
//	}
//	@PostMapping(value = "/uploadAjaxAction", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//	@ResponseBody
//	public ResponseEntity<List<AttachFileDTO>> uploadAjaxPost(MultipartFile[] uploadFile) {
//
//		List<AttachFileDTO> list = new ArrayList<>();
//		String uploadFolder = "C:\\upload";
//
//		String uploadFolderPath = getFolder();
//		// make folder --------
//		File uploadPath = new File(uploadFolder, uploadFolderPath);
//		if (!uploadPath.exists()) {
//			uploadPath.mkdirs();
//			System.out.println("HI");
//		}
//		// make yyyy/MM/dd folder
//		
//
//		for (MultipartFile multipartFile : uploadFile) {
//
//			AttachFileDTO attachDTO = new AttachFileDTO();
//
//			String uploadFileName = multipartFile.getOriginalFilename();
//			System.out.println(uploadFileName);
//			// IE has file path c:\\ace\\img\\1.jpg
//			uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf("\\") + 1);
//			
//			attachDTO.setFileName(uploadFileName);
//			System.out.println(uploadFileName);
//
//			UUID uuid = UUID.randomUUID();
//
//			uploadFileName = uuid.toString() + "_" + uploadFileName;
//
//			try {
//				File saveFile = new File(uploadPath, uploadFileName);
//				
//
//				attachDTO.setUuid(uuid.toString());
//				attachDTO.setUploadPath(uploadFolderPath);
//
//				// check image type file
//				if (checkImageType(saveFile)) {
//
//					attachDTO.setImage(true);
//					System.out.println(uploadPath);
//					FileOutputStream thumbnail = new FileOutputStream(new File(uploadPath, "s_" + uploadFileName));
//					System.out.println(uploadFileName);
//					Thumbnailator.createThumbnail(multipartFile.getInputStream(), thumbnail, 100, 100);
//					multipartFile.transferTo(saveFile);
//
//					System.out.println("111");
//					thumbnail.close();
//					System.out.println("222");
//				}
//
//				// add to List
//				list.add(attachDTO);
//				System.out.println(list);
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//
//		} // end for
//		return new ResponseEntity<>(list, HttpStatus.OK);
//	}
//
//	@GetMapping("/display")
//	@ResponseBody
//	public ResponseEntity<byte[]> getFile(String fileName) {
//
//		System.out.println("ho2");
//		File file = new File("c:\\upload\\" + fileName);
//
//
//		ResponseEntity<byte[]> result = null;
//
//		try {
//			HttpHeaders header = new HttpHeaders();
//
//			header.add("Content-Type", Files.probeContentType(file.toPath()));
//			result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return result;
//	}
//
//
//}
