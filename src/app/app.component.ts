import { Injectable } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { Couples } from './models/couples';
import { CoupleService } from './couple-service';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

@Injectable({
  providedIn: 'root'
})

export class AppComponent implements OnInit {
  selectCouple: any = {};
  coupleList: any[] = []; // Variable para almacenar los registros
  isEditing: boolean = false;


  constructor(private coupleService: CoupleService) { }

  ngOnInit(): void {
    this.loadCouples(); // Llama a la función para cargar los registros al inicializar el componente
  }

  AddOrUpdate() {
    if (this.isEditing) {
      if (this.selectCouple && this.selectCouple.partner1name && this.selectCouple.partner2name) {

        // Modo de edición: Actualiza el registro existente
        // Aquí debes implementar la lógica para actualizar el registro en tu base de datos
        // Por ejemplo, si tienes un servicio CoupleService, podrías llamar a un método updateCouple
        // y pasar this.selectCouple como argumento.
        this.coupleService.updateCouple(this.selectCouple).subscribe(() => {
          // Lógica adicional después de la actualización si es necesario
          // Por ejemplo, cargar la lista de parejas nuevamente.
          this.loadCoupleList();
          this.selectCouple = new Couples();
        });
      } else {
        // Los campos están vacíos, muestra un mensaje de error o realiza alguna acción
        if(confirm('Los campos no pueden estar vacíos')){
          this.isEditing=false;
        }
      }

    } else {
      if (this.selectCouple && this.selectCouple.partner1name && this.selectCouple.partner2name) {
        // Los campos no están vacíos, puedes proceder con la inserción
        this.coupleService.createCouple(this.selectCouple).subscribe(() => {
          // Lógica adicional después de la creación si es necesario
          // Por ejemplo, cargar la lista de parejas nuevamente.
          this.loadCoupleList();
          this.selectCouple = new Couples();
        });
      } else {
        // Los campos están vacíos, muestra un mensaje de error o realiza alguna acción
        console.log(alert('Los campos no pueden estar vacíos'));
      }

    }
  }


  loadCouples() {
    this.coupleService.getAllCouples().subscribe(
      (data) => {
        this.coupleList = data; // Almacena los registros en la variable coupleList
        console.log(this.coupleList);

      },
      (error) => {
        console.error('Error al cargar los registros:', error);
      }
    );
  }


  OpenEdit(couple: Couples) {
    this.selectCouple = couple; // Clonar el registro seleccionado
    if (this.selectCouple && this.selectCouple.partner1name && this.selectCouple.partner2name) {

    this.isEditing = true; // Habilita el modo de actualización si los campos no están vacíos
    }else{
      this.isEditing=false;
    }
  }


  // Método para eliminar un registro
  deleteCouple() {
    if (confirm("Are you sure you want to delete it?")) {
      this.coupleService.deleteCoupleById(this.selectCouple.cod_couple).subscribe(() => {
        // Después de eliminar, vuelve a cargar la lista actualizada
        this.loadCoupleList();
        this.selectCouple = {}; // Limpia selectCouple

      });
    }
  }

  // Función para cargar la lista de parejas desde el servidor
  loadCoupleList() {
    this.coupleService.getAllCouples().subscribe((data: any) => {
      this.coupleList = data; // Actualiza la lista de parejas con los datos del servidor
    });
  }

}
