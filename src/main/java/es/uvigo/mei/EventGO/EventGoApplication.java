package es.uvigo.mei.EventGO;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.AbstractPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import daos.UsuarioDAO;
import entidades.Usuario;

@SpringBootApplication
@ComponentScan({ "controllers", "servicios", "jwt", "seguridad"})
@EnableJpaRepositories("daos")
@EntityScan("entidades")
public class EventGoApplication implements CommandLineRunner {

	@Autowired
	UsuarioDAO dao;

	@Autowired
	PasswordEncoder passwordEncoder;

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	public static void main(String[] args) {
		SpringApplication.run(EventGoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Creacion de usuarios de ejemplo

		if (!dao.existsByLogin("juan")) {
			Usuario juan = new Usuario("juan", passwordEncoder.encode("Juan1"), "Juan Juanez", "juan@juan.com", "ROLE_USUARIO", "26648533C", "2000-05-22", "España", "");
			dao.save(juan);
		}

		if (!dao.existsByLogin("ana")) {
			Usuario ana = new Usuario("ana", passwordEncoder.encode("Ana2"), "Ana Anido", "ana@ana.com", "ROLE_USUARIO", "77598433K", "2000-05-21", "España", "");
			dao.save(ana);
		}
		if (!dao.existsByLogin("pedro")) {
			Usuario pedro = new Usuario("pedro", passwordEncoder.encode("Pedro3"), "Pedro Pedrez", "pedro@pedro.com", "ROLE_GERENTE", "48772598V", "2000-05-20", "España", "");
			dao.save(pedro);
		}

		if (!dao.existsByLogin("admin")) {
			Usuario admin = new Usuario("admin", passwordEncoder.encode("Admin1"), "Administrador", "admin@admin.com", "ROLE_ADMINISTRADOR", "46743507D", "2000-05-19", "España", "");
			dao.save(admin);
		}
		System.out.println("Servidor Spring operativo");
	}
}
