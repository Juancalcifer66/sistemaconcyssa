package com.concyssa.sistemaconcyssa.service.impl;

import com.concyssa.sistemaconcyssa.service.FileStorageService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final Path fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();

    public FileStorageServiceImpl() {
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("No se pudo crear el directorio donde se almacenarán los archivos.", ex);
        }
    }

    @Override
    public String almacenarArchivo(MultipartFile file) {
        String nombreOriginal = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            if (nombreOriginal.contains("..")) {
                throw new RuntimeException("El nombre del archivo contiene una ruta inválida: " + nombreOriginal);
            }

            // Generar un nombre único para evitar colisiones
            String extension = "";
            int index = nombreOriginal.lastIndexOf('.');
            if (index > 0) {
                extension = nombreOriginal.substring(index);
            }
            String nombreUnico = UUID.randomUUID().toString() + extension;

            Path targetLocation = this.fileStorageLocation.resolve(nombreUnico);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return nombreUnico;
        } catch (IOException ex) {
            throw new RuntimeException("No se pudo almacenar el archivo " + nombreOriginal + ". Intente nuevamente.", ex);
        }
    }

    @Override
    public Resource cargarComoRecurso(String nombreArchivo) {
        try {
            Path filePath = this.fileStorageLocation.resolve(nombreArchivo).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("Archivo no encontrado: " + nombreArchivo);
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException("Archivo no encontrado: " + nombreArchivo, ex);
        }
    }

    @Override
    public void eliminarArchivo(String nombreArchivo) {
        try {
            Path filePath = this.fileStorageLocation.resolve(nombreArchivo).normalize();
            Files.deleteIfExists(filePath);
        } catch (IOException ex) {
            throw new RuntimeException("No se pudo eliminar el archivo: " + nombreArchivo, ex);
        }
    }
}