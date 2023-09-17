package autenticacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import daos.UsuarioDAO;
import entidades.Usuario;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UsuarioDAO usuarioDAO;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Usuario usuario = usuarioDAO.findFirstByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("No se encuentra un usuario con el login " + login));

        return new UserDetailsImpl(usuario);
    }

}

