package com.bit2015.fileupload.controller;

import java.io.FileOutputStream;
import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileUploadController {
	
	private static final Log LOG = LogFactory.getLog( FileUploadController.class );
	private static final String SAVE_PATH = "/temp";					// 미리 약속을 해둬야함
	  
	@RequestMapping( "/form" )
	public String form() {
		return "form";
	}
	
	@RequestMapping( "/upload" )
	public String upload( @RequestParam String email, @RequestParam( "file1" ) MultipartFile file1, Model model ) {
        
		// 보통 파라미터의 값
		LOG.debug( " ######## email : " + email );

		// 첫 번째 파일 처리
		if( file1.isEmpty() == false ) {
			
	        String fileOriginalName = file1.getOriginalFilename();		// 사용자가 업로드한 파일 이름.(ex. 지출.txt)
	        String extName = fileOriginalName.substring( fileOriginalName.lastIndexOf(".") + 1, fileOriginalName.length() );
	        String saveFileName = genSaveFileName( extName );			// 저장할 파일 이름(구분이 쉽게 보통 날짜로 많이 함) => 유일해야 함!
	        Long size = file1.getSize();
	        
	        writeFile( file1, SAVE_PATH, saveFileName );				// file1안에 InputStream이 들어가 있음

	        String url = "/product-images/" + saveFileName;				// ★★★ 저장한 경로를 주지않고 쌩뚱맞은 폴더 이름으로 해놓은 이유는
	        model.addAttribute( "productImageUrl1", url );				// spring-servlet참조, mapping시켜 놓음
	
	        LOG.debug( " ######## fileOriginalName : " + fileOriginalName );
	        LOG.debug( " ######## fileSize : " + size );
	        LOG.debug( " ######## fileExtensionName : " + extName );
	        LOG.debug( " ######## saveFileName : " + saveFileName );        
		}
		
        return "result";
	}
	
	private void writeFile( MultipartFile file, String path, String fileName ) {
		FileOutputStream fos = null;
		try {
			byte fileData[] = file.getBytes();					// getBytes():업로드한 파일 데이터를 구하는 가장 단순한 방법
			fos = new FileOutputStream( path + "/" + fileName );
			fos.write(fileData);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (Exception e) {
				}
			}
		}
	}
	
	private String genSaveFileName( String extName ) {
		
        Calendar calendar = Calendar.getInstance();
		String fileName = "";
        
        fileName += calendar.get( Calendar.YEAR );
        fileName += calendar.get( Calendar.MONTH );
        fileName += calendar.get( Calendar.DATE );
        fileName += calendar.get( Calendar.HOUR );
        fileName += calendar.get( Calendar.MINUTE );
        fileName += calendar.get( Calendar.SECOND );
        fileName += calendar.get( Calendar.MILLISECOND );
        fileName += ( "." + extName );
        
        return fileName;
	}
}
