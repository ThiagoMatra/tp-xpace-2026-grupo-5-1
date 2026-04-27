import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.assertions.throwables.shouldThrow
import java.time.LocalDate
import org.uqbar.geodds.Point

class TripulanteTest : DescribeSpec({

    describe ("Test de tripulantes"){

        context("Calculo del tripulante Gorosito"){
            val gorosito= Tripulante().apply{
                nombre = "Victor"
                apellido = "Gorosito"
                fechaNacimiento= LocalDate.of(1976, 3, 5)
                misionesExitosas= 2
                misionesParciales= 6
                misionesFracasadas= 4
                fechaInicio= LocalDate.of(2012, 4, 16)
            }

            it ("Deberia tener 50 anios de edad"){
                gorosito.edad() shouldBe 50
            }
            it("Deberia estar activo hace 14 años"){
                gorosito.aniosActivo() shouldBe 14
            }
            it ("Deberia tener 18 de experiencia"){
                gorosito.experiencia() shouldBe 18
            }
        }

        context("Calculo del tripulante Baez"){
            val baez= Tripulante().apply{
                apellido="Baez"
                fechaNacimiento=LocalDate.of(2002,3,12)
                misionesExitosas=2
                misionesParciales=4
                misionesFracasadas=2
                fechaInicio= LocalDate.of(2024,1,4)
            }
            it ("Deberia no tener 25 anios de edad"){
                baez.edad() shouldNotBe 25
            }
            it("Deberia estar activo hace 2 años"){
                baez.aniosActivo() shouldBe 2
            }
            it ("Deberia tener 5 de experiencia"){
                baez.experiencia() shouldBe 5
            }
        }
        
        it("Deberia tener 7 de experiencia"){
            val trip = Tripulante().apply(){
                fechaInicio = LocalDate.now().minusYears(3)
                misionesExitosas = 4
                misionesFracasadas = 2
                misionesParciales = 4
            }
            trip.experiencia() shouldBe 7
        }

        it("Deberia tener 10 de experiencia"){
            val trip2 = Tripulante().apply{
                fechaInicio = LocalDate.now().minusYears(5)
                misionesExitosas = 2
                misionesFracasadas = 4
                misionesParciales = 8
            }
            trip2.experiencia() shouldBe 10
        }

        it("Deberia tener 13 de experiencia"){
            val tripulanteManu = Tripulante().apply{
                misionesExitosas = 10
                misionesFracasadas = 4
                misionesParciales = 8
                fechaInicio = LocalDate.now().minusYears(4)
            }
            tripulanteManu.experiencia() shouldBe 13
        }     
    }



    describe("Verificacion de Roles y Perfiles de Aptitud "){
        val planetaEstable = Planeta().apply{
            fueAterrizado = true
            nivelRadiacion = 10.0F
            temperaturaMedia= 20.0F
            distanciaTierra = 10.0F
            gravedad = 10.0F
        }

        val naveModerna = Sonda().apply{
            fechaFabricacion = LocalDate.of(2025, 1, 1)
        }
        val naveVieja = Sonda().apply{
            fechaFabricacion = LocalDate.of(2010, 1, 1)
        }


        val misionGrupal = Mision().apply{
            estado = EstadoMision.COMPLETADA
            planetaDestino = planetaEstable
            naveAsignada = naveModerna
        }
        val misionMala = Mision().apply{
            estado= EstadoMision.FALLIDA
            planetaDestino = planetaEstable
            naveAsignada = naveVieja
        }

        val comandante = Tripulante().apply{
            salarioBase = 100.0F
            fechaInicio = LocalDate.now().minusYears(4)
            rol = Comandante()
            asignarMision(misionGrupal)
            asignarMision(misionMala)

        }
        it ("Comandante con 2 Misiones exitosas calcula bien Bonus"){

            comandante.salarioTotal() shouldBe 155F // 100 + 50 + 5
            comandante.historialMisiones.size shouldBe 2
            comandante.misionesExitosas shouldNotBe 3
        }

        val piloto = Tripulante().apply{
            salarioBase = 100.0F
            perfilAptitud= Cauteloso(umbral = 15)
            fechaInicio = LocalDate.now().minusYears(4)
            //Piloto.asignarMision(misionGrupal)
        }

        it ("Piloto con perfil Cauteloso acepta Mision ") {
            piloto.salarioTotal() shouldNotBe 160F // 100 + 30
            piloto.esAptoMision(misionGrupal) shouldBe true  //umbral mas chico que radiacion
        }

        it ("Ingeniero cobra 20% si su ultima mision no fue Carguero"){
            val ingeniero = Tripulante().apply{
                rol = Ingeniero()
                salarioBase = 100.0F
                asignarMision(misionGrupal)
            }
            ingeniero.salarioTotal() shouldBe 120F
        }

        it("Ingeniero cobra 40% de bono si su ultima mision completada fue en carguero") {
            val misionPasada = Mision().apply {
                estado = EstadoMision.COMPLETADA
                naveAsignada = Carguero() // Fuerza el 40%
            }

            val ingeniero = Tripulante().apply {
                rol = Ingeniero()
                salarioBase = 100F
                historialMisiones.add(misionPasada)
            }

            ingeniero.salarioTotal() shouldBe 140F
        }



        val cientifico = Tripulante().apply{
            rol = Cientifico()
            salarioBase= 100F
            fechaInicio = LocalDate.now().minusYears(4)
            perfilAptitud = Exigente()
            asignarMision(misionGrupal)
        }
        it(" Cientifico con perfil exigente con la nave "){
            cientifico.esAptoMision(misionGrupal) shouldBe true //nave moderna
            cientifico.salarioTotal() shouldNotBe 120F
            cientifico.esAptoMision(misionMala) shouldBe false
        }

        val medico = Tripulante().apply{
            rol = Medico()
            salarioBase= 100F
            perfilAptitud= Prudente()
            fechaInicio = LocalDate.now().minusYears(4)
            asignarMision(misionMala)
        }

        it("Medico con Aptitud Prudente "){
            medico.esAptoMision(misionGrupal) shouldBe true
            medico.salarioTotal() shouldBe 127 // 100 salario base + 25% salario base + 2 mision fallida
         }
    }


    describe("Bases cercana o no a Tripulante"){
        val tripulanteTierra = Tripulante().apply{
            direccion.ubicacion = Point(0.0 , 0.0)
            distanciaMaximaCercania = 100.0
        }
        it("Base Cercana"){
            val baseCercana = BaseLanzamiento().apply{
                // Creo que es con grados, 1 grado en X son 111 km, aca son como 80km
                direccion.ubicacion = Point(0.5 , 0.3)
            }
            tripulanteTierra.consideraCercana(baseCercana) shouldBe true
        }
        it("Base Lejana"){
            val baseLejana = BaseLanzamiento().apply{
                direccion.ubicacion = Point(1.0 , 1.0)
            }
            tripulanteTierra.consideraCercana(baseLejana) shouldBe false
        }
    }

    describe("Tripulantes Validos"){
        it("Tripulante cargado con todo") {
            val tripulanteValido = Tripulante().apply {
                nombre = "Ellen"
                apellido = "Ripley"
                fechaNacimiento = LocalDate.of(1980, 1, 8)
                baseAsignada = BaseLanzamiento()
                rol = Piloto()
            }
            tripulanteValido.esValido() shouldBe true
        }

        it("Un tripulante es inválido si su fecha de nacimiento es en el futuro") {
            val tripulanteInvalido = Tripulante().apply {
                nombre = "Ellen"
                apellido = "Ripley"
                fechaNacimiento = LocalDate.now().plusYears(5) // Fecha futura (falla aquí)
                baseAsignada = BaseLanzamiento()
                rol = Piloto()
            }
            tripulanteInvalido.esValido() shouldBe false
        }
    }
    describe("validaciones de tripulantes"){
        it("Es valido si tiene datos clave") {
            val baseNueva = BaseLanzamiento().apply{}
            val TripulanteApto = Tripulante().apply {
                nombre = "Gome"
                apellido = "Uri"
                fechaNacimiento = LocalDate.of(2002, 5, 2)
                rol = Piloto()
                baseAsignada = baseNueva

            }
        TripulanteApto.esValido() shouldBe true
        }

        it("No es valido si le faltan datos"){
            val TripulanteNoApto = Tripulante().apply{}
            TripulanteNoApto.esValido() shouldBe false
            TripulanteNoApto.nombreValido() shouldBe false
            TripulanteNoApto.apellidoValido() shouldBe false
            TripulanteNoApto.fechaNacimientoValida() shouldBe false
            TripulanteNoApto.rolValido() shouldBe false
            }
        }


    describe("Validacion de PerfilCompuesto"){
        it("Debe tener 2 Perfiles y aceptar todas los requisitios de los perfiles"){
            val listaPerfiles = listOf(Conformista(),Cauteloso (20))
            val misionnueva = Mision().apply{
                planetaDestino = Planeta().apply{
                    nivelRadiacion = 10.0F
                }
            }
            val tripulanteCompuestoValido = Tripulante().apply{
                perfilAptitud = PerfilCompuestoTodos(listaPerfiles) // agregar 2 roles
            }
            tripulanteCompuestoValido.perfilAptitud.aceptaMision(misionnueva) shouldBe true
            }

    it("Debe tener 1 perfil y dar error en en Init") {
        val listaInvalida = listOf(Conformista())

        val excepcion = shouldThrow<IllegalArgumentException> { PerfilCompuestoAlguno(listaInvalida) }
        excepcion.message shouldBe "Perfil compuesto debe recibir solo 2 aptitudes"
    }

        it ("Debe tener 2 perfiles y debe aceptar 1 requisito de un rol"){
            val listaPerfiles = listOf(Conformista(),Cauteloso (5))
            val misionnueva = Mision().apply{
                planetaDestino = Planeta().apply{
                    nivelRadiacion = 10.0F
                }
            }
            val tripulanteCompuestoValido = Tripulante().apply{
                perfilAptitud = PerfilCompuestoAlguno(listaPerfiles) // agregar 2 roles
            }
            tripulanteCompuestoValido.perfilAptitud.aceptaMision(misionnueva) shouldBe true
        }

        it("Debe tener 2 Perfiles y no aceptar 1 requisito de las aptitudes"){
            val listaPerfiles = listOf(Conformista(),Cauteloso (5))
            val misionnueva = Mision().apply{
                planetaDestino = Planeta().apply{
                    nivelRadiacion = 10.0F
                }
            }
            val tripulanteCompuestoValido = Tripulante().apply{
                perfilAptitud = PerfilCompuestoTodos(listaPerfiles) // agregar 2 roles
            }
            tripulanteCompuestoValido.perfilAptitud.aceptaMision(misionnueva) shouldBe false
        }

        it("Perfil cambiante con año par e impar"){
            val misionnueva = Mision().apply{
                planetaDestino = Planeta().apply{ nivelRadiacion = 1500.0F ; gravedad = 300.0F ; hayAguaLiquida = false}
                }
            val tripulante22 = Tripulante().apply{
                fechaNacimiento = LocalDate.of(2004,2,5)
                perfilAptitud = PerfilCambiante(this)
            }
            val tripulante23 = Tripulante().apply{
                fechaNacimiento = LocalDate.of(2003,2,24)
                perfilAptitud = PerfilCambiante(this)
            }

            tripulante22.perfilAptitud.aceptaMision(misionnueva) shouldBe true
            tripulante23.perfilAptitud.aceptaMision(misionnueva) shouldBe false

            }


        }





})
