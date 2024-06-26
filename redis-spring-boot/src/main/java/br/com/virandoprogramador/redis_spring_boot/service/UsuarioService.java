package br.com.virandoprogramador.redis_spring_boot.service;

import br.com.virandoprogramador.redis_spring_boot.exception.UsuarioNaoEncontradoException;
import br.com.virandoprogramador.redis_spring_boot.model.ModelUsuario;
import br.com.virandoprogramador.redis_spring_boot.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Cacheable(value = "ModelUsuario")
    public List<ModelUsuario> listarUsuarios(){
        return usuarioRepository.findAll();
    }

    @Cacheable(value = "ModelUsuario", key = "#id")
    public ModelUsuario pesquisaPorId(Long id){
        Optional<ModelUsuario>  modelUsuario = usuarioRepository.findById(id);
        return modelUsuario.orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario não encontrado"));
    }

    public ModelUsuario salvarUsuario(ModelUsuario modelUsuario){
        return usuarioRepository.save(modelUsuario);
    }

    @CachePut(value = "ModelUsuario", key = "#id")
    public ModelUsuario alterarUsuario(ModelUsuario modelUsuario, Long id){
        ModelUsuario usuarioAlterado = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario não encontrado"));

        usuarioAlterado.setNome(modelUsuario.getNome());
        usuarioAlterado.setEmail(modelUsuario.getEmail());
        return usuarioRepository.save(usuarioAlterado);
    }

    @CacheEvict(value = "ModelUsuario", key = "#id")
    public void deletarUsuario(Long id){
        ModelUsuario modelUsuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario não encontrado"));
        usuarioRepository.deleteById(modelUsuario.getId());
    }

}