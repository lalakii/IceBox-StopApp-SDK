import com.android.build.gradle.internal.tasks.AarMetadataTask

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    `maven-publish`
    signing
    id("cn.lalaki.central") version "1.2.5"
}
android {
    namespace = "cn.lalaki.sdk"
    compileSdk = 35
    version = 1.2
    buildTypes {
        named("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions.jvmTarget = "17"
    buildToolsVersion = "35.0.0"
    defaultConfig {
        minSdk = 19
    }
}
dependencies {
    implementation("androidx.annotation:annotation-jvm:1.8.0")
    implementation("androidx.core:core-ktx:1.13.1")
}
tasks.withType<AarMetadataTask> {
    isEnabled = false
}
tasks.configureEach {
    if (name.contains("checkDebugAndroidTestAarMetadata", ignoreCase = true)) {
        enabled = false
    }
}
centralPortalPlus {
    username = System.getenv("TEMP_USER")
    password = System.getenv("TEMP_PASS")
}
signing {
    useGpgCmd()
    sign(publishing.publications)
}
publishing {
    repositories {
        maven {
            name = "localPluginRepository"
            val publishToLocal = true
            if (publishToLocal) {
                url = uri("D:\\repo\\")
            }
        }
    }
    publications {
        create<MavenPublication>("release") {
            val githubUrl = "https://github.com/lalakii/IceBox-StopApp-SDK"
            artifactId = "3rd-party-sdk"
            groupId = "cn.lalaki"
            afterEvaluate { artifact(tasks.named("bundleReleaseAar")) }
            pom.withXml {
                val dependenciesNode = asNode().appendNode("dependencies")
                configurations.implementation.get().dependencies.forEach { dependency ->
                    if (dependency.version != "unspecified" && dependency.name != "unspecified") {
                        val dependencyNode = dependenciesNode.appendNode("dependency")
                        dependencyNode.appendNode("groupId", dependency.group)
                        dependencyNode.appendNode("artifactId", dependency.name)
                        dependencyNode.appendNode("version", dependency.version)
                    } else {
                        println(">>> [WARN] Excluded module: " + dependency.group + ":" + dependency.name)
                    }
                }
            }
            pom {
                name = "IceBox&StopApp-SDK"
                description = "IceBox&StopApp-SDK 冰箱和小黑屋SDK，冻结、解冻应用实现。"
                url = githubUrl
                licenses {
                }
                developers {
                    developer {
                        name = "lalakii"
                        email = "dazen@189.cn"
                    }
                }
                organization {
                    name = "lalakii"
                    url = "https://lalaki.cn"
                }
                licenses {
                    license {
                        name = "MIT License"
                        url = "https://opensource.org/license/mit"
                    }
                }
                scm {
                    url = githubUrl
                    connection = "scm:git:$githubUrl.git"
                    developerConnection = "scm:git:$githubUrl.git"
                }
            }
        }
    }
}
