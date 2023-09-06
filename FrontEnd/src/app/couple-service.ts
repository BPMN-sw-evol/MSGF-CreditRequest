import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CoupleService {
  private apiUrl = 'http://localhost:3000'; // URL completa del servidor Express

  constructor(private http: HttpClient) {}

  createCouple(selectCouple: any): Observable<any> {
    // Envia los datos del formulario al servidor en la ruta /registrar
    return this.http.post(`${this.apiUrl}/registrar`, selectCouple);
  }

   // Método para actualizar una pareja existente
   updateCouple(couple: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/actualizar/${couple.cod_couple}`, couple);
  }

  getAllCouples(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/allcouples`);
  }

  // Método para eliminar un registro por su ID
  deleteCoupleById(cod_couple: number) {
    return this.http.delete(`${this.apiUrl}/eliminar/${cod_couple}`);
  }

}
