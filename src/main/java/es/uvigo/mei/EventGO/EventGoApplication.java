package es.uvigo.mei.EventGO;

import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;

import daos.UsuarioDAO;
import daos.CategoriaDAO;
import daos.EventoDAO;
import daos.SuscripcionDAO;
import daos.SolicitudDAO;
import daos.AmistadDAO;
import daos.ComentarioDAO;
import daos.NoticiaDAO;
import entidades.Usuario;
import entidades.Categoria;
import entidades.Evento;
import entidades.Suscripcion;
import entidades.Solicitud;
import entidades.Amistad;
import entidades.Comentario;
import entidades.Noticia;

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
	EventoDAO eventoDAO;

	@Autowired
	SuscripcionDAO suscripcionDAO;

	@Autowired
	SolicitudDAO solicitudDAO;

	@Autowired
	AmistadDAO amistadDAO;

	@Autowired
	ComentarioDAO comentarioDAO;

	@Autowired
	NoticiaDAO noticiaDAO;

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
			Usuario juan = new Usuario("juan", passwordEncoder.encode("Juan1"), "Juan Juanez", "gilblancomartin@gmail.com", "ROLE_USUARIO", "26648533C", "2000-05-22", "España", "http://localhost:8080/api/media/juan.jpeg", "0");
			usuarioDAO.save(juan);
		}

		if (!usuarioDAO.existsByLogin("ana")) {
			Usuario ana = new Usuario("ana", passwordEncoder.encode("Ana2"), "Ana Anido", "gilblanco@gmx.es", "ROLE_USUARIO", "77598433K", "2000-05-21", "España", "http://localhost:8080/api/media/ana.jpeg", "0");
			usuarioDAO.save(ana);
		}
		if (!usuarioDAO.existsByLogin("pedro")) {
			Usuario pedro = new Usuario("pedro", passwordEncoder.encode("Pedro3"), "Pedro Pedrez", "pedro@gmail.com", "ROLE_GERENTE", "48772598V", "2000-05-20", "España", "http://localhost:8080/api/media/pedro.jpeg", "0");
			usuarioDAO.save(pedro);

			if(!categoriaDAO.existsByNombre("Música")) {
				Categoria musica = new Categoria("Música", "Encontrarás eventos musicales y mucho más.", "http://localhost:8080/api/media/musica.jpg", "0");
				categoriaDAO.save(musica);

				if(categoriaDAO.existsByNombre("Música")) {
					Evento eventoMusica = new Evento("Concierto C.Tangana", "Album El Madrileño. Celebrando el éxito del nuevo album el día 25 de noviembre a las 19:30.", "PUBLICO", 30000, 0, "ABIERTO", "2024-11-25", "Av. de Concha Espina, 1, 28036 Madrid", "cTanganaConcierto@gmail.com", "698726119", musica, pedro, "http://localhost:8080/api/media/ctangana.jpg", "http://localhost:8080/api/media/ctanganaConcierto.pdf", "https://www.tomaticket.es/es-es/artista/c-tangana", "0");
					eventoDAO.save(eventoMusica);

					Evento eventoMusica2 = new Evento("Concierto Beret", "Junto a todos los fans de Beret en el Madrid arena el 25 de diciembre.", "PUBLICO", 20000, 0, "ABIERTO", "2024-12-25", "Av. de Rodolfo García, 1, 24167 Madrid", "beret@gmail.com", "694589322", musica, pedro, "http://localhost:8080/api/media/beret.jpg", "http://localhost:8080/api/media/beretConcierto.pdf", "https://www.tomaticket.es/es-es/artista/beret", "0");
					eventoDAO.save(eventoMusica2);

					Usuario juan = usuarioDAO.findByLogin("juan").get();
					Suscripcion suscripcion1 = new Suscripcion(eventoMusica, juan, "2023-12-08");
					suscripcionDAO.save(suscripcion1);

					Amistad amistad1 = new Amistad(pedro, juan);
					amistadDAO.save(amistad1);

					Comentario comentario1 = new Comentario(5, "Muy buen evento", juan, eventoMusica);
					comentarioDAO.save(comentario1);

					Noticia noticia1 = new Noticia("Primera Noticia", "He publicado dos nuevos eventos de música, si os interesan anotaros pronto.", "2023-12-08", pedro);
					noticiaDAO.save(noticia1);
				}
			}
		}

		if (!usuarioDAO.existsByLogin("mateo")) {
			Usuario mateo = new Usuario("mateo", passwordEncoder.encode("Mateo4"), "Mateo Blanco", "mateo@gmail.com", "ROLE_GERENTE", "11299081D", "1990-04-25", "España", "http://localhost:8080/api/media/mateo.jpeg", "0");
			usuarioDAO.save(mateo);


			if(!categoriaDAO.existsByNombre("Ártes escénicas")) {
				Categoria artesEscenicas = new Categoria("Ártes escénicas", "Encontrarás eventos relacionados con cine, teatro o artes visuales.", "http://localhost:8080/api/media/artesEscenicas.jpg", "0");
				categoriaDAO.save(artesEscenicas);

				if(categoriaDAO.existsByNombre("Ártes escénicas")) {
					Evento eventoEscenico = new Evento("Romeo y julieta", "Obra teatral de Romeo y Julieta. El 25 de abril a las 17:00.", "PRIVADO", 300, 0, "ABIERTO","2024-04-25", "Teatro del Mar, 1, 28036 Barcelona", "romeoJulieta@gmail.com", "698726119", artesEscenicas, mateo, "http://localhost:8080/api/media/rome&Julieta.jpg", "http://localhost:8080/api/media/romeo&JulietaActuacion.pdf", "https://www.teatrepoliorama.com/es/programacion/c/757-romeu-i-julieta.html", "0");
					eventoDAO.save(eventoEscenico);

					Evento eventoEscenico2 = new Evento("Don Quijote", "Obra teatral de Don Quijote de la Mancha. El 25 de mayo a las 17:00.", "PRIVADO", 200, 0, "ABIERTO","2024-05-25", "Palau Sant Jordi, 1, 28194 Barcelona", "donQuijote@gmail.com", "649283491", artesEscenicas, mateo, "http://localhost:8080/api/media/donQuijote.jpg", "http://localhost:8080/api/media/donQuijote.pdf", "https://www.teatroespanol.es/espectaculo/yo-soy-don-quijote-de-la-mancha", "0");
					eventoDAO.save(eventoEscenico2);

					Usuario juan = usuarioDAO.findByLogin("juan").get();
					Solicitud solicitud1 = new Solicitud(eventoEscenico, juan, "2023-12-08");
					solicitudDAO.save(solicitud1);
				}
			}
		}

		if (!usuarioDAO.existsByLogin("marcos")) {
			Usuario marcos = new Usuario("marcos", passwordEncoder.encode("Marcos5"), "Marcos García", "marcos@gmail.com", "ROLE_GERENTE", "15884297Z", "1990-04-27", "España", "http://localhost:8080/api/media/marcos.jpeg", "1");
			usuarioDAO.save(marcos);
		}

		if (!usuarioDAO.existsByLogin("nacho")) {
			Usuario marcos = new Usuario("nacho", passwordEncoder.encode("Nacho6"), "Nacho conde", "nacho@gmail.com", "ROLE_GERENTE", "87829180T", "1990-05-27", "España", "http://localhost:8080/api/media/nacho.jpeg", "0");
			usuarioDAO.save(marcos);
		}

		if (!usuarioDAO.existsByLogin("admin")) {
			Usuario admin = new Usuario("admin", passwordEncoder.encode("Admin1"), "Administrador", "admin@admin.com", "ROLE_ADMINISTRADOR", "46743507D", "2000-05-19", "España", "", "0");
			usuarioDAO.save(admin);
		}

		if(!categoriaDAO.existsByNombre("Deportes")) {
			Categoria deportes = new Categoria("Deportes", "Encontrarás eventos y actividades deportivas.", "http://localhost:8080/api/media/deportes.jpg", "0");
			categoriaDAO.save(deportes);
		}

		if(!categoriaDAO.existsByNombre("Gastronomía y fiestas")) {
			Categoria gastronomiaFiestas = new Categoria("Gastronomía y fiestas", "Encontrarás eventos y actividades deportivas.", "http://localhost:8080/api/media/gastronomia.jpg", "0");
			categoriaDAO.save(gastronomiaFiestas);
		}

		if(!categoriaDAO.existsByNombre("Negocios")) {
			Categoria negocios = new Categoria("Negocios", "Encontrarás eventos de negocios, charlas o talleres de finanzas.", "http://localhost:8080/api/media/negocios.jpg", "0");
			categoriaDAO.save(negocios);
		}

		if(!categoriaDAO.existsByNombre("Educación")) {
			Categoria educacion = new Categoria("Educación", "Encontrarás eventos o actividades educativas.", "http://localhost:8080/api/media/educativas.jpeg","0");
			categoriaDAO.save(educacion);
		}

		if(!categoriaDAO.existsByNombre("Viajes")) {
			Categoria viajes = new Categoria("Viajes", "Encontrarás eventos de viajes o excursiones.", "http://localhost:8080/api/media/viajes.jpg", "0");
			categoriaDAO.save(viajes);
		}

		if(!categoriaDAO.existsByNombre("Visitas culturales")) {
			Categoria visitasCulturales = new Categoria("Visitas culturales", "Encontrarás eventos de viajes o excursiones.", "http://localhost:8080/api/media/visitasCulturales.jpg", "0");
			categoriaDAO.save(visitasCulturales);
		}

		if(!categoriaDAO.existsByNombre("Otros")) {
			Categoria otros = new Categoria("Otros", "Otros eventos disponibles.", "http://localhost:8080/api/media/otros.jpg", "0");
			categoriaDAO.save(otros);
		}

		System.out.println("Servidor Spring operativo");
	}
}
