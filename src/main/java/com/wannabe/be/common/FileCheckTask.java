package com.wannabe.be.common;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.wannabe.be.product.dao.ProductAttachMapper;
import com.wannabe.be.product.vo.ProductAttachVO;

import lombok.Setter;

@Component
public class FileCheckTask {

	@Setter(onMethod_ = { @Autowired })
	private ProductAttachMapper attachMapper;

	private String getFolderYesterDay() {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Calendar cal = Calendar.getInstance();

		cal.add(Calendar.DATE, -1);

		String str = sdf.format(cal.getTime());

		return str.replace("-", File.separator);
	}

	//@Scheduled(cron = "10 * * * * *")
	//@Scheduled(fixedDelay=1000*60*60)
	public void checkFiles() throws Exception {
		System.out.println("@Scheduled");
//		log.warn("File Check Task run.................");
//		log.warn(new Date());
		// file list in database
		List<ProductAttachVO> fileList = attachMapper.getOldFiles();

		// ready for check file in directory with database file list
		List<Path> fileListPaths = fileList.stream()
				.map(vo -> Paths.get("C:\\upload", vo.getUploadpath(), vo.getUuid() + "_" + vo.getFilename()))
				.collect(Collectors.toList());

		// image file has thumnail file
		fileList.stream()
				.map(vo -> Paths.get("C:\\upload", vo.getUploadpath(), "s_" + vo.getUuid() + "_" + vo.getFilename()))
				.forEach(p -> fileListPaths.add(p));

	//	log.warn("===========================================");

		//fileListPaths.forEach(p -> log.warn(p));

		// files in yesterday directory
		File targetDir = Paths.get("C:\\upload", getFolderYesterDay()).toFile();

		File[] removeFiles = targetDir.listFiles(file -> fileListPaths.contains(file.toPath()) == false);

	//	log.warn("-----------------------------------------");
		for (File file : removeFiles) {

			//log.warn(file.getAbsolutePath());

			file.delete();

		}
	}
}
