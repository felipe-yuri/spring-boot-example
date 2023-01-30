package br.com.sankhya.packagerepository.controllers.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

import br.com.sankhya.packagerepository.models.Curso;
import br.com.sankhya.packagerepository.models.Topico;
import br.com.sankhya.packagerepository.repositories.CursoRepository;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class TopicoForm {

	@NotNull @NotEmpty @Size(min = 5)
	private String titulo;
	
	@NotNull @NotEmpty @Size(min = 5)
	private String mensagem;
	
	@NotNull @NotEmpty
	private String nomeCurso;
	
	public Topico converter(CursoRepository repository) {
		Curso curso = repository.findByNome(nomeCurso);
		return new Topico(titulo, mensagem, curso);
	}

}
