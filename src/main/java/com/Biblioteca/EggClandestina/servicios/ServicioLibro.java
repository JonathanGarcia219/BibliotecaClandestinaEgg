package com.Biblioteca.EggClandestina.servicios;

import com.Biblioteca.EggClandestina.entidades.EntidadLibro;
import com.Biblioteca.EggClandestina.entidades.EntidadLibro;
import com.Biblioteca.EggClandestina.errores.ErrorServicio;
import com.Biblioteca.EggClandestina.repositorios.RepositorioLibro;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service//Declaramos con esta notación que es un servicio
//El servicio se encarga de toda la logica del App
public class ServicioLibro {

    //CADA VEZ QUE NECESITEMOS EL REPOSITORIO LO INSTANCIA EL AUTOWIRED
    @Autowired
    private RepositorioLibro repoLibro;

    @Autowired
    private ServicioAutor servAutor;

    @Autowired
    private ServicioEditorial servEdit;

    @Transactional
    public EntidadLibro crearLibro(EntidadLibro libro) throws ErrorServicio {

        if (libro.getAnio() == null || libro.getAnio().toString().length() < 4 || libro.getAnio() > 1400 && libro.getAnio() < Calendar.YEAR) {
            throw new ErrorServicio("Error al indicar el año.");
        }

        if (libro.getEmplares() == null || libro.getEmplares() < 1) {
            throw new ErrorServicio("Error al indicar la cantidad de ejemplares");
        }

        if (libro.getIsbn() == null || libro.getIsbn().length() < 10 || libro.getIsbn().isEmpty()) {
            throw new ErrorServicio("Error al ingresar el ISBN");
        }

        if (libro.getTitulo() == null || libro.getTitulo().contains("  ") || libro.getTitulo().length() < 3) {
            throw new ErrorServicio("Error al indicar el titulo");
        }

        if (libro.getAutor() == null) {
            throw new ErrorServicio("Debe indicar un Autor");
        }

        if (libro.getEditorial() == null) {
            throw new ErrorServicio("Debe indicar una Editorial");
        }

        libro.setAlta(Boolean.TRUE);

        return repoLibro.save(libro);

    }

    @Transactional(readOnly = true)
    public Optional<EntidadLibro> buscarPorId(String id) {

        return repoLibro.findById(id);
    }

    @Transactional(readOnly = true)
    public List<EntidadLibro> buscaTodos() {

        return repoLibro.findAll();

    }

    @Transactional
    public void modificar(EntidadLibro dto) throws ErrorServicio {
        EntidadLibro libro = repoLibro.findById(dto.getId()).orElseThrow(() -> new ErrorServicio(String.format("MENSAJE", dto.getId())));
        libro.setIsbn(dto.getIsbn());
        libro.setTitulo(dto.getTitulo());
        libro.setAnio(dto.getAnio());
        libro.setEmplares(dto.getEmplares());
        libro.setAutor(dto.getAutor());
        libro.setEditorial(dto.getEditorial());
        repoLibro.save(libro);
    }
}
