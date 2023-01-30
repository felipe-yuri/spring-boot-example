package br.com.sankhya.packagerepository.controllers;

import java.net.URI;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.sankhya.packagerepository.controllers.dto.DetalhesDoTopicoDto;
import br.com.sankhya.packagerepository.controllers.dto.TopicoDto;
import br.com.sankhya.packagerepository.controllers.form.AtualizarTopicoForm;
import br.com.sankhya.packagerepository.controllers.form.TopicoForm;
import br.com.sankhya.packagerepository.models.Topico;
import br.com.sankhya.packagerepository.repositories.CursoRepository;
import br.com.sankhya.packagerepository.repositories.TopicoRepository;

@RestController
@RequestMapping("/topicos")
public class TopicosController {

	@Autowired
	private TopicoRepository topicoRepository;
	@Autowired
	private CursoRepository cursoRepository;
	
	@GetMapping
	@Cacheable(value = "findAll")
	public Page<TopicoDto> findAll(@RequestParam(required = false) String nomeCurso,
			@PageableDefault(sort = "dataCriacao", direction = Direction.DESC, page = 0, size = 2) Pageable paginacao) {
		
		Page<Topico> topicos = null;
		if (nomeCurso == null) {
			topicos = topicoRepository.findAll(paginacao);
			return TopicoDto.converter(topicos);
		}

		topicos = topicoRepository.findByCursoNome(nomeCurso, paginacao);
		return TopicoDto.converter(topicos);
	}

	@GetMapping("/{id}")
	@Cacheable(value = "detailsById")
	public ResponseEntity<DetalhesDoTopicoDto> detailsById(@PathVariable Long id) {
		Optional<Topico> topico = topicoRepository.findById(id);
		if (topico.isPresent()) {
			return ResponseEntity.ok(new DetalhesDoTopicoDto(topico.get()));
		}
		return ResponseEntity.notFound().build();
	}

	@PostMapping
	@Transactional
	@CacheEvict(value = {"findAll", "detailsById"}, allEntries = true)
	public ResponseEntity<TopicoDto> insert(@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriBuilder) {
		Topico topico = form.converter(cursoRepository);
		topicoRepository.save(topico);
		URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
		return ResponseEntity.created(uri).body(new TopicoDto(topico));
	}

	@PutMapping("/{id}")
	@Transactional
	@CacheEvict(value = {"findAll", "detailsById"}, allEntries = true)
	public ResponseEntity<TopicoDto> update(@PathVariable Long id, @RequestBody @Valid AtualizarTopicoForm form) {
		Optional<Topico> optional = topicoRepository.findById(id);
		if (optional.isPresent()) {
			Topico topico = form.atualizar(id, topicoRepository);
			return ResponseEntity.ok(new TopicoDto(topico));
		}
		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{id}")
	@Transactional
	@CacheEvict(value = {"findAll", "detailsById"}, allEntries = true)
	public ResponseEntity<TopicoDto> delete(@PathVariable Long id) {
		Optional<Topico> optional = topicoRepository.findById(id);
		if (optional.isPresent()) {
			topicoRepository.deleteById(id);
			return ResponseEntity.ok(null);
		}
		return ResponseEntity.notFound().build();
	}
}
