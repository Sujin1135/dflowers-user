dependencies {
    implementation(project(":subproject:interface"))
    implementation(project(":subproject:presentation"))
    implementation(project(":subproject:domain"))
    implementation(project(":subproject:application"))
    implementation(project(":subproject:infrastructure"))
}

springBoot {
    mainClass.set("io.dflowers.user.UserApplicationKt")
}
