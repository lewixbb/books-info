package pl.czytajto.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.zip.Deflater;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import pl.czytajto.books.model.BookCoverImage;
import pl.czytajto.books.repository.BookCoverImageRepository;

@RestController
@RequestMapping("/service/eer")
public class ServiceController {

	@Autowired
	BookCoverImageRepository bookCoverImageRepository;
	
	@PostMapping("/upload")

	public ResponseEntity<?> uploadImage(@RequestParam(value = "file") MultipartFile file) throws IOException {
		
		System.out.println("start");
		System.out.println("Orginal Image Byte Size = " + file.getBytes().length);
		
		BookCoverImage img = new BookCoverImage(file.getOriginalFilename(), file.getContentType(), 
				file.getBytes());
//			compressBytes(file.getBytes()));
		
		BookCoverImage newImg = bookCoverImageRepository.save(img);
		
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/addBook")
				.buildAndExpand(newImg.getId())
				.toUri();
		return ResponseEntity.created(location).body(file);	
	}

	private byte[] compressBytes(byte[] data) {

		Deflater deflater = new Deflater();
		deflater.setInput(data);
		deflater.finish();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
		byte[] buffer = new byte[1024];
		while(!deflater.finished()) {
			int count = deflater.deflate(buffer);
			outputStream.write(buffer,0,count);
		}try {
			outputStream.close();
		}catch(IOException e) {		
		}
		System.out.println("Compresed Image Byte Size = " + outputStream.toByteArray().length);		
		return outputStream.toByteArray();
	}
	
	
	
	
}
