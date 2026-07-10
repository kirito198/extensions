import com.lagradost.cloudstream3.gradle.CloudstreamExtension

extensions.getByName<CloudstreamExtension>("cloudstream").apply {
    name = "Krmizi"
    description = "Extension for Krmizi streaming website"
    authors = listOf("Kirito198")
    version = 1
}
