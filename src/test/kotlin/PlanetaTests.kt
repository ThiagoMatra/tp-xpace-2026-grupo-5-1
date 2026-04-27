import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate

class PlanetaTest : DescribeSpec({

    describe("Test de condiciones habitables") {

        it("Planeta Manu deberia tener temperatura ideal y gravedad soportable") {
            val planetaManu = Planeta().apply {
                nombre = "Manu"
                temperaturaMedia = 22.2F
                gravedad = 14F
            }

            planetaManu.idealHumano() shouldBe true
            planetaManu.gravedadSoportable() shouldBe true
        }

        it("Jupiter deberia tener temperatura ideal y gravedad soportable") {
            val jupiter = Planeta().apply {
                nombre = "Jupiter"
                temperaturaMedia = 33.3F
                gravedad = 15F
            }

            jupiter.idealHumano() shouldBe true
            jupiter.gravedadSoportable() shouldBe true
        }

        it("deberia tener toxicidad soportable si la toxicidad es menor a 30") {
            val planeta = Planeta().apply {
                nombre = "Planeta Verde"
                toxicidadAtmosfera = 20.0F
            }

            planeta.toxicidadSoportable() shouldBe true
        }

        it("deberia tener radiacion soportable si la radiacion es menor a 40") {
            val planeta = Planeta().apply {
                nombre = "Planeta Azul"
                nivelRadiacion = 25.0F
            }

            planeta.radiacionSoportable() shouldBe true
        }

        it("deberia ser habitable si cumple todas las condiciones") {
            val planeta = Planeta().apply {
                nombre = "Tierra 2"
                temperaturaMedia = 25.0F
                gravedad = 9.0F
                hayAguaLiquida = true
                toxicidadAtmosfera = 10.0F
                nivelRadiacion = 20.0F
            }

            planeta.esHabitable() shouldBe true
        }
    }

    describe("Test de condiciones no habitables") {

        it("Osiris deberia ser no habitable") {
            val osiris = Planeta().apply {
                nombre = "Osiris"
                temperaturaMedia = 60.5F
                gravedad = 13.0F
                nivelRadiacion = 5.0F
                hayAguaLiquida = true
                toxicidadAtmosfera = 2.7F
                actividadTectonica = 45.8F
                tamanio = 1349.56F
                fechaDescubrimiento = LocalDate.of(1965, 11, 5)
                distanciaTierra = 896.7F
                fueAterrizado = true
            }

            osiris.idealHumano() shouldBe false
            osiris.gravedadSoportable() shouldBe true
            osiris.esHabitable() shouldBe false
        }

        it("Saturno deberia ser no habitable") {
            val saturno = Planeta().apply {
                nombre = "Saturno"
                temperaturaMedia = 45.2F
                gravedad = 10F
            }

            saturno.idealHumano() shouldBe false
            saturno.gravedadSoportable() shouldBe true
            saturno.esHabitable() shouldBe false
        }

        it("Marte deberia ser no habitable") {
            val marte = Planeta().apply {
                nombre = "Marte"
                temperaturaMedia = 90.0F
                gravedad = 16.0F
                nivelRadiacion = 50.0F
                hayAguaLiquida = true
                toxicidadAtmosfera = 98.1F
                tamanio = 989.2F
            }

            marte.idealHumano() shouldBe false
            marte.gravedadSoportable() shouldBe false
            marte.esHabitable() shouldBe false
        }

        it("no deberia tener toxicidad soportable si la toxicidad es 30 o mayor") {
            val planeta = Planeta().apply {
                nombre = "Planeta Toxico"
                toxicidadAtmosfera = 35.0F
            }

            planeta.toxicidadSoportable() shouldBe false
        }

        it("no deberia tener radiacion soportable si la radiacion es 40 o mayor") {
            val planeta = Planeta().apply {
                nombre = "Planeta Radiactivo"
                nivelRadiacion = 40.0F
            }

            planeta.radiacionSoportable() shouldBe false
        }

        it("no deberia ser habitable si no tiene agua liquida") {
            val planeta = Planeta().apply {
                nombre = "Secolandia"
                temperaturaMedia = 20.0F
                gravedad = 9.0F
                hayAguaLiquida = false
                toxicidadAtmosfera = 10.0F
                nivelRadiacion = 10.0F
            }

            planeta.esHabitable() shouldBe false
        }
    }

    describe("Test de exploracion y peligrosidad") {

        it("deberia calcular correctamente el indice de peligrosidad") {
            val planeta = Planeta().apply {
                nombre = "Peligro"
                nivelRadiacion = 30.0F
                toxicidadAtmosfera = 60.0F
                actividadTectonica = 90.0F
            }

            planeta.indicePeligrosidad() shouldBe 60.0F
        }

        it("deberia ser explorable si no es habitable y su indice de peligrosidad es menor a 60") {
            val planeta = Planeta().apply {
                nombre = "Explorax"
                temperaturaMedia = 80.0F
                gravedad = 10.0F
                hayAguaLiquida = false
                toxicidadAtmosfera = 20.0F
                nivelRadiacion = 20.0F
                actividadTectonica = 20.0F
            }

            planeta.esHabitable() shouldBe false
            planeta.esExplorable() shouldBe true
        }

        it("no deberia ser explorable si su indice de peligrosidad es 60 o mayor") {
            val planeta = Planeta().apply {
                nombre = "Inferno"
                temperaturaMedia = 80.0F
                gravedad = 10.0F
                hayAguaLiquida = false
                toxicidadAtmosfera = 90.0F
                nivelRadiacion = 90.0F
                actividadTectonica = 90.0F
            }

            planeta.esHabitable() shouldBe false
            planeta.esExplorable() shouldBe false
        }
    }

    describe("Test de validacion y aterrizaje") {

        // -------- nombreValido --------
        it("nombreValido deberia ser false si el nombre esta vacio") {
            Planeta().apply {
                nombre = ""
            }.nombreValido() shouldBe false
        }

        // -------- tamanioValido --------
        it("tamanioValido deberia ser false si el tamaño es 0 o menor") {
            Planeta().apply {
                tamanio = 0.0F
            }.tamanioValido() shouldBe false
        }

        // -------- gravedadValida --------
        it("gravedadValida deberia ser true si la gravedad es mayor a 0") {
            Planeta().apply {
                gravedad = 9.8F
            }.gravedadValida() shouldBe true
        }

        it("gravedadValida deberia ser false si la gravedad es 0 o menor") {
            Planeta().apply {
                gravedad = 0.0F
            }.gravedadValida() shouldBe false
        }

        // -------- toxicidadValida --------
        it("toxicidadValida deberia ser false si es menor a 0") {
            Planeta().apply {
                toxicidadAtmosfera = -1.0F
            }.toxicidadValida() shouldBe false
        }

        it("toxicidadValida deberia ser false si es mayor a 100") {
            Planeta().apply {
                toxicidadAtmosfera = 101.0F
            }.toxicidadValida() shouldBe false
        }

        // -------- radiacionValdia --------
        it("radiacionValdia deberia ser true si esta entre 0 y 100") {
            Planeta().apply {
                nivelRadiacion = 50.0F
            }.radiacionValdia() shouldBe true
        }

        // -------- esValido --------
        it("esValido deberia ser true cuando todos los campos son validos") {
            val planeta = Planeta().apply {
                nombre = "Pandora"
                tamanio = 150.0F
                gravedad = 9.8F
                toxicidadAtmosfera = 25.0F
                nivelRadiacion = 30.0F
            }

            planeta.esValido() shouldBe true
        }

        it("esValido deberia ser false si algun campo es invalido") {
            val planeta = Planeta().apply {
                nombre = ""
                tamanio = 150.0F
                gravedad = 9.8F
                toxicidadAtmosfera = 25.0F
                nivelRadiacion = 30.0F
            }

            planeta.esValido() shouldBe false
        }

        // -------- registrarAterrizaje --------
        it("deberia registrar el aterrizaje") {
            val planeta = Planeta().apply {
                nombre = "Pandora"
                fueAterrizado = false
            }

            planeta.registrarAterrizaje()

            planeta.fueAterrizado shouldBe true
        }
    }
})