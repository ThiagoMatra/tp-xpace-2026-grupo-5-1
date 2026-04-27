import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import java.time.LocalDate

class RepositoriosTests : DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest

    describe("Tests metodos genericos (add, delete)") {
        val repositorioTripulantes = Repositorio<Tripulante>()
        val pedro = Tripulante().apply{
            nombre = "Pedro"
            apellido = "Perez"}


        repositorioTripulantes.create(Tripulante().apply{nombre = "Juan"})
        repositorioTripulantes.create(pedro)


        it("Agregar miembro (Metodo create)"){
            repositorioTripulantes.coleccion.size shouldBe 2
        }
        it("Eliminar miembro (Metodo delete)"){
            repositorioTripulantes.delete(pedro)
            repositorioTripulantes.coleccion.size shouldBe 1
        }
        it("Obtener miembro por ID"){
            repositorioTripulantes.getById(2) shouldBe pedro
        }
        it("Actualizar miembro") {
            pedro.nombre = "Pedro Modificado"
            repositorioTripulantes.update(pedro)
            repositorioTripulantes.getById(2).nombre shouldBe "Pedro Modificado"
        }


    }
    describe("Tests metodos search"){
        val planeta = Planeta().apply{nombre = "Andromeda"}
        val tripulante = Tripulante().apply{nombre = "Pedro" ; apellido = "Perez"}
        val nave = Sonda().apply{codigoID = "0132"}
        val mision = Mision().apply{nombre = "MisionExploracion" ; planetaDestino = planeta}
        val baseLanzamiento = BaseLanzamiento().apply{nombre = "BaseTierra" ; direccion.pais = "Argentina"}
        val subvencion = Subvencion().apply{nombre = "PlanBajo"}

        val repositorioTripulante = Repositorio<Tripulante>()
        repositorioTripulante.create(Tripulante())
        repositorioTripulante.create(tripulante)
        val repositorioPlaneta = Repositorio<Planeta>()
        repositorioPlaneta.create(Planeta())
        repositorioPlaneta.create(planeta)
        val repositorioNave = Repositorio<NaveEspacial>()
        repositorioNave.create(Sonda())
        repositorioNave.create(nave)
        val repositorioMision = Repositorio<Mision>()
        repositorioMision.create(Mision())
        repositorioMision.create(mision)
        val repositorioBase = Repositorio<BaseLanzamiento>()
        repositorioBase.create(BaseLanzamiento())
        repositorioBase.create(baseLanzamiento)
        val repositorioSubvencion = Repositorio<Subvencion>()
        repositorioSubvencion.create(Subvencion())
        repositorioSubvencion.create(subvencion)

        it("Tripulante busca nombre parcial"){
            repositorioTripulante.search("Pe").first() shouldBe tripulante
        }
        it("Tripulante busca apellido completo"){
            repositorioTripulante.search("Perez").first() shouldBe tripulante
        }
        it("Planeta busca nombre parcial"){
            repositorioPlaneta.search("meda").first() shouldBe planeta
        }
        it("Nave busca codigo exacto"){
            repositorioNave.search("0132").first() shouldBe nave
        }
        it("Nave no devuelve resultado con codigo parcial"){
            repositorioNave.search("013").size shouldBe 0
        }
        it("Mision busca nombre exacto"){
            repositorioMision.search("MisionExploracion").first() shouldBe mision
        }
        it("Mision busca por nombre de planeta destino"){
            repositorioMision.search("Andromeda").first() shouldBe mision
        }
        it("Base busca nombre parcial"){
            repositorioBase.search("BaseTi").first() shouldBe baseLanzamiento
        }
        it("Base busca pais exacto"){
            repositorioBase.search("Argentina").first() shouldBe baseLanzamiento
        }
        it("Subvencion busca por comienzo del nombre"){
            repositorioSubvencion.search("Plan").first() shouldBe subvencion
        }
        it("Subvencion no devuelve resultado si no empieza con el valor"){
            repositorioSubvencion.search("Bajo").size shouldBe 0
        }




    }
})