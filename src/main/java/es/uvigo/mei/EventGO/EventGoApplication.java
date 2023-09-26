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
import daos.CategoriaDAO;
import entidades.Usuario;
import entidades.Categoria;

@SpringBootApplication
@ComponentScan({ "controllers", "servicios", "jwt", "seguridad"})
@EnableJpaRepositories("daos")
@EntityScan("entidades")
public class EventGoApplication implements CommandLineRunner {

	@Autowired
	UsuarioDAO usuarioDAO;

	@Autowired
	CategoriaDAO categoriaDAO;

	@Autowired
	PasswordEncoder passwordEncoder;

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	public static void main(String[] args) {
		SpringApplication.run(EventGoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Creacion de usuarios de ejemplo

		if (!usuarioDAO.existsByLogin("juan")) {
			Usuario juan = new Usuario("juan", passwordEncoder.encode("Juan1"), "Juan Juanez", "juan@juan.com", "ROLE_USUARIO", "26648533C", "2000-05-22", "España", "");
			usuarioDAO.save(juan);
		}

		if (!usuarioDAO.existsByLogin("ana")) {
			Usuario ana = new Usuario("ana", passwordEncoder.encode("Ana2"), "Ana Anido", "ana@ana.com", "ROLE_USUARIO", "77598433K", "2000-05-21", "España", "");
			usuarioDAO.save(ana);
		}
		if (!usuarioDAO.existsByLogin("pedro")) {
			Usuario pedro = new Usuario("pedro", passwordEncoder.encode("Pedro3"), "Pedro Pedrez", "pedro@pedro.com", "ROLE_GERENTE", "48772598V", "2000-05-20", "España", "");
			usuarioDAO.save(pedro);
		}

		if (!usuarioDAO.existsByLogin("admin")) {
			Usuario admin = new Usuario("admin", passwordEncoder.encode("Admin1"), "Administrador", "admin@admin.com", "ROLE_ADMINISTRADOR", "46743507D", "2000-05-19", "España", "");
			usuarioDAO.save(admin);
		}

		if(!categoriaDAO.existsByNombre("Música")) {
			Categoria musica = new Categoria("Música", "Podrás encontrar eventos musicales y mucho más.", "http://localhost:8080/api/media/musica.jpg");
			categoriaDAO.save(musica);
		}

		if(!categoriaDAO.existsByNombre("Ártes escénicas")) {
			Categoria artesEscenicas = new Categoria("Ártes escénicas", "Podrás encontrar eventos relacionados con cine, teatro o artes visuales.", "http://localhost:8080/api/media/artesEscenicas.jpg");
			categoriaDAO.save(artesEscenicas);
		}

		if(!categoriaDAO.existsByNombre("Deportes")) {
			Categoria deportes = new Categoria("Deportes", "Podrás encontrar eventos y actividades deportivas.", "http://localhost:8080/api/media/deportes.jpg");
			categoriaDAO.save(deportes);
		}

		if(!categoriaDAO.existsByNombre("Gastronomía y fiestas")) {
			Categoria gastronomiaFiestas = new Categoria("Gastronomía y fiestas", "Podrás encontrar eventos y actividades deportivas.", "http://localhost:8080/api/media/gastronomia.jpg");
			categoriaDAO.save(gastronomiaFiestas);
		}

		if(!categoriaDAO.existsByNombre("Negocios")) {
			Categoria negocios = new Categoria("Negocios", "Podrás encontrar eventos de negocios, charlas o talleres de finanzas.", "http://localhost:8080/api/media/negocios.jpg");
			categoriaDAO.save(negocios);
		}

		if(!categoriaDAO.existsByNombre("Educación")) {
			Categoria educacion = new Categoria("Educación", "Podrás encontrar eventos o actividades educativas.", "http://localhost:8080/api/media/educativas.jpeg");
			categoriaDAO.save(educacion);
		}

		if(!categoriaDAO.existsByNombre("Viajes")) {
			Categoria viajes = new Categoria("Viajes", "Podrás encontrar eventos de viajes o excursiones.", "http://localhost:8080/api/media/viajes.jpg");
			categoriaDAO.save(viajes);
		}

		if(!categoriaDAO.existsByNombre("Visitas culturales")) {
			Categoria visitasCulturales = new Categoria("Visitas culturales", "Podrás encontrar eventos de viajes o excursiones.", "http://localhost:8080/api/media/visitasCulturales.jpg");
			categoriaDAO.save(visitasCulturales);
		}

		if(!categoriaDAO.existsByNombre("Otros")) {
			Categoria otros = new Categoria("Otros", "Otros eventos disponibles.", "http://localhost:8080/api/media/otros.jpg");
			categoriaDAO.save(otros);
		}

		System.out.println("Servidor Spring operativo");
	}
}
