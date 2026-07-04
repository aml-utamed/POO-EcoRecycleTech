# POO-EcoRecycleTech

## Datos del alumno
- **Nombre:** Adrián Murciego Lobato
- **Asignatura:** Programación Orientada a Objetos (POO)
- **Universidad:** UTAMED
- **Curso:** 2025-2026

## Descripción
Aplicación Java para el control del sistema automatizado de la planta de reciclaje
y separación de residuos de **EcoRecycle Tech SA**.

Implementa:
- Modelo de dominio con interfaces, clases abstractas y herencia (polimorfismo).
- Patrón creacional **Factory Method** (`ResiduoFactory`).
- Arquitectura **Modelo-Vista-Controlador (MVC)** con desacoplamiento estricto.
- Interfaz gráfica con **Java Swing**.
- Persistencia de estado (`estado_planta.json`) y log de operaciones (`recycle.log`).

## Estructura del proyecto
```
src/main/java/
├── Main.java                  # Punto de entrada
├── modelo/
│   ├── IResiduo.java          # Interfaz
│   ├── Residuo.java           # Clase abstracta
│   ├── ResiduoPlastico.java   # Concreto: Plástico
│   ├── ResiduoVidrio.java     # Concreto: Vidrio
│   ├── ResiduoPapel.java      # Concreto: Papel
│   ├── ResiduoMetal.java      # Concreto: Metal (extensión SOLID)
│   ├── Contenedor.java        # Contenedor de almacenamiento
│   ├── ResiduoFactory.java    # Patrón Factory
│   └── PlantaReciclaje.java   # Modelo principal
├── vista/
│   └── PlantaVista.java       # GUI Swing
└── controlador/
    └── PlantaControlador.java # Controlador MVC
```

## Compilación y ejecución
```bash
# Compilar
javac -d out src/main/java/modelo/*.java src/main/java/vista/*.java \
      src/main/java/controlador/*.java src/main/java/Main.java

# Ejecutar
java -cp out Main

# Generar Javadoc
javadoc -d docs -sourcepath src/main/java -subpackages modelo:vista:controlador \
        -encoding UTF-8 -charset UTF-8 src/main/java/Main.java
```

## Extensibilidad demostrada
Se añadió `ResiduoMetal` y su contenedor `Depósito de Metal` sin modificar
ninguna clase base existente, demostrando el Principio Abierto/Cerrado (OCP).
