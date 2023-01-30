package br.com.sankhya.packagerepository.controllers.dto;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

import br.com.sankhya.packagerepository.models.Topico;
import lombok.Getter;
@Getter
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class TopicoDto {

	private Long id;
	private String title;
	private String message;
	private LocalDateTime creationDate;

	public TopicoDto(Topico topico) {
		this.id = topico.getId();
		this.title = topico.getTitulo();
		this.message = topico.getMensagem();
		this.creationDate = topico.getDataCriacao();
	}

	public static Page<TopicoDto> converter(Page<Topico> topicos) {
		return topicos.map(TopicoDto::new);
	}
}
