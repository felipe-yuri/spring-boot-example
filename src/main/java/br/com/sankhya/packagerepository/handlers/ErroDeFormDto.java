package br.com.sankhya.packagerepository.handlers;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErroDeFormDto {
	private String campo;
	private String erro;
}
