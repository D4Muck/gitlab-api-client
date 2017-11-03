package at.d4m.gitlab.client

import org.reactivestreams.Publisher
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/**
 * @author Christoph Muck
 */
class GitlabApiClient(
        baseUrl: String,
        privateToken: String
) {

    private val webClient = WebClient.builder()
            .baseUrl(baseUrl + "/api/v4")
            .defaultHeader("PRIVATE-TOKEN", privateToken)
            .build()

    private fun get(uri: String): WebClient.ResponseSpec {
        return webClient.get().uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
    }

    private fun delete(uri: String, vararg uriVariables: Any): WebClient.ResponseSpec {
        return webClient.delete().uri(uri, *uriVariables)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
    }

    private fun <T> post(uri: String, body: Publisher<T>): WebClient.ResponseSpec {
        return webClient.post().uri(uri)
                .body(body, object : ParameterizedTypeReference<T>() {})
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
    }

    fun getUserProfiles(): Flux<User> {
        return get("/users").bodyToFlux(User::class.java)
    }

    fun createProject(project: Publisher<Project>): Mono<Project> {
        return post("/projects", project).bodyToMono(Project::class.java)
    }

    fun createProject(project: Project): Mono<Project> {
        return createProject(Mono.just(project))
    }

    fun getProjects(): Flux<Project> {
        return get("/projects").bodyToFlux(Project::class.java)
    }

    fun removeProject(id: Long): Mono<String> {
        return delete("/projects/{id}", id).bodyToMono(String::class.java)
    }
}

