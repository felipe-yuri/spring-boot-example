package br.com.sankhya.packagerepository.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.sankhya.packagerepository.models.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

}
