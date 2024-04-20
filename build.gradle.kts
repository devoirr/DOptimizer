plugins {
    id("java")
    id("io.papermc.paperweight.userdev") version "1.5.12"
}

group = "me.kervand"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    paperweight.paperDevBundle("1.17.1-R0.1-SNAPSHOT")
}

tasks.reobfJar {
    outputJar.set(File("/Users/roman/IdeaProjects/DOptimizer/jar/DOptimizer.jar"))
}
