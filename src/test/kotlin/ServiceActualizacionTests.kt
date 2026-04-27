import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import io.mockk.mockk
import io.mockk.every
import io.mockk.verify


class ServiceActualizacionTests : DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest

    describe("Llamar a un servicio"){
        // Dentro de tu test
        val mockService = mockk<ServicePlanetas>()
        val repoPlanetas = Repositorio<Planeta>()
        val planeta = Planeta().apply{id = 1 ; nombre = "PlanetaTest"}
        repoPlanetas.create(planeta)

        // Le dices al mock qué JSON devolver cuando el servicio llame a getPlanetas()
        every { mockService.getPlanetas() } returns """
[
  {
    "id": 1,
    "nombre": "Kepler-442b",
    "temperaturaMedia": 22.5,
    "gravedad": 9.8,
    "nivelRadiacion": 15,
    "hayAguaLiquida": true,
    "toxicidadAtmosfera": 10,
    "actividadTectonica": 20,
    "tamanio": 7500,
    "distanciaTierra": 104.7
  },
  {
    "nombre": "Proxima Centauri b",
    "temperaturaMedia": -39.0,
    "gravedad": 11.2,
    "nivelRadiacion": 75,
    "hayAguaLiquida": false,
    "toxicidadAtmosfera": 60,
    "actividadTectonica": 45,
    "tamanio": 7160,
    "distanciaTierra": 4.24
  }
]
""".trimIndent()

        val servicio = ServiceActualizacion(mockService, repoPlanetas)
        servicio.actualizar()


        it("Deberia crearse id 2"){
            repoPlanetas.getById(2).nombre shouldBe "Proxima Centauri b"
        }
        it("Deberia actualizarse id 1"){
            repoPlanetas.getById(1).nombre shouldBe "Kepler-442b"
        }
    }





})