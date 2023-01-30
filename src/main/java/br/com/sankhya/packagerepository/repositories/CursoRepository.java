package br.com.sankhya.packagerepository.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.sankhya.packagerepository.models.Curso;

public interface CursoRepository extends JpaRepository<Curso, Long>{
	Curso findByNome(String nomeCurso);
}
