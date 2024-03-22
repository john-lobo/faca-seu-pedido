pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Faça seu pedido"
include(":features:app")
include(":data:baseservice")
include(":features:coreandroid")
include(":data:productservice")
include(":data:firebaseservice")
