// package com.ecommerce.educative.service.product;

// import org.apache.commons.io.FilenameUtils;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.core.io.Resource;
// import org.springframework.core.io.UrlResource;
// import org.springframework.stereotype.Service;
// import org.springframework.web.multipart.MultipartFile;
// import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

// import com.ecommerce.educative.exception.customExceptions.BadRequestException;
// import com.ecommerce.educative.exception.customExceptions.NotFoundException;
// import com.ecommerce.educative.model.product.FileInfo;
// import com.ecommerce.educative.repository.FileRepository;

// import java.io.IOException;
// import java.io.InputStream;
// import java.net.MalformedURLException;
// import java.nio.file.Files;
// import java.nio.file.Path;
// import java.nio.file.Paths;
// import java.nio.file.StandardCopyOption;
// import java.util.Optional;
// import java.util.UUID;
// import java.util.stream.Stream;

// @Service
// public class FileService {

//   @Autowired
//   FileRepository fileRepository;

//   // Path rootLocation = Paths.get();

//   public String store(MultipartFile file) {
//     Path rootLocation = Paths.get();
//     // Optional<FileInfo> optionalFile= fileRepository.findById(file.)
//     try {
//       if (file.isEmpty()) {
//         throw new NotFoundException("File not found.");
//       }
//       // find extension of the file,png or jpg
//       String extension = FilenameUtils.getExtension(file.getOriginalFilename());

//       // generate a random unique name for the image
//       String uploadedFileName = UUID.randomUUID().toString() + "." + extension;

//       // create a path for destination file
//       Path destinationFile = rootLocation.resolve(Paths.get(uploadedFileName))
//           .normalize().toAbsolutePath();

//       // Copy input file to destination file path
//       try (InputStream inputStream = file.getInputStream()) {
//         Files.copy(inputStream, destinationFile,
//             StandardCopyOption.REPLACE_EXISTING);

//         final String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

//         // create the public Image URl where we can find the image
//         final StringBuilder imageStringBuilder = new StringBuilder(baseUrl);
//         imageStringBuilder.append("/fileUpload/files/");
//         imageStringBuilder.append(uploadedFileName);

//         return imageStringBuilder.toString();
//       }
//     } catch (IOException e) {
//       throw new BadRequestException("Failed to upload file.");
//     }
//   }

//   public Stream<Path> loadAll() {
//     // load all the files
//     try {
//       return Files.walk(this.rootLocation, 1)
//           // ignore the root path
//           .filter(path -> !path.equals(this.rootLocation))
//           .map(this.rootLocation::relativize);
//     } catch (IOException e) {
//       throw new BadRequestException("Failed to read stored files");
//     }

//   }

//   public Resource load(String filename) {
//     try {
//       // read the file based on the filename
//       Path file = rootLocation.resolve(filename);
//       // get resource from path
//       Resource resource = new UrlResource(file.toUri());

//       if (resource.exists() || resource.isReadable()) {
//         return resource;
//       } else {
//         throw new RuntimeException("Could not read the file!");
//       }
//     } catch (MalformedURLException e) {
//       throw new RuntimeException("Error: " + e.getMessage());
//     }
//   }
// }
