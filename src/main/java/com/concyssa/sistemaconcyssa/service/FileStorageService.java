package com.concyssa.sistemaconcyssa.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String almacenarArchivo(MultipartFile file);
    Resource cargarComoRecurso(String nombreArchivo);
    void eliminarArchivo(String nombreArchivo);
}