import { Component } from '@angular/core';
import { CoupleService } from '../couple-service';
import { Couples } from '../models/couples';

@Component({
  selector: 'app-view-app',
  templateUrl: './view-app.component.html',
  styleUrls: ['./view-app.component.css']
})
export class ViewAppComponent {
  coupleList: any[] = []; // Variable para almacenar los registros
  selectCouple: any = {};
  isEditing: boolean = false;

  constructor(private coupleService: CoupleService) { }


  ngOnInit(): void {
    this.loadCouples(); // Llama a la función para cargar los registros al inicializar el componente
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
