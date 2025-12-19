package com.kurumi.lab3;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:80", "http://localhost")
                        .allowedMethods("GET", "POST");
            }
        };
    }
}

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
class TimeClick {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int clicks;
}

interface ClickRepository extends JpaRepository<TimeClick, Long> {
}

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:80", "http://localhost"})
class ClickController {
    private final ClickRepository repository;

    public ClickController(ClickRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/status")
    public TimeClick getStatus() {
        return repository.findById(1L).orElseGet(() -> repository.save(new TimeClick(1L, 0)));
    }

    @PostMapping("/click")
    public TimeClick addClick() {
        TimeClick click = repository.findById(1L).orElse(new TimeClick(1L, 0));
        click.setClicks(click.getClicks() + 1);
        return repository.save(click);
    }

    @PostMapping("/reset")
    public TimeClick resetClick() {
        TimeClick click = repository.findById(1L).orElse(new TimeClick(1L, 0));
        click.setClicks(0);
        return repository.save(click);
    }
}