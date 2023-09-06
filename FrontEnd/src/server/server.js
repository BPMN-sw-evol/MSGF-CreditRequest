const express = require('express');
const app = express();
const port = process.env.PORT || 3000;
const bodyParser = require('body-parser');
const { Pool } = require('pg');
const cors = require('cors'); // Importa el módulo cors


const pool = new Pool({
  user: 'postgres',
  host: 'localhost',
  database: 'msgfoundation',
  password: 'admin',
  port: 5432,
});

const corsOptions = {
    origin: 'http://localhost:4200', // Cambia esto a tu origen de Angular
    optionsSuccessStatus: 200, // Algunas versiones de navegadores antiguos (IE) no devuelven 204
  };
  
app.use(cors(corsOptions));

app.use(bodyParser.json());

// Ruta para obtener todos los registros de la tabla 'couple'
// Ruta para obtener todos los registros de la tabla 'couple'
app.get('/allcouples', async (req, res) => {
    try {
      // Realiza una consulta SQL para obtener todos los registros de la tabla 'couple'
      const query = 'SELECT * FROM couple';
      const result = await pool.query(query);
  
      // Extrae los registros de la respuesta de la base de datos
      const couples = result.rows;
  
      // Envía los registros como respuesta en formato JSON
      res.json(couples);
    } catch (error) {
      console.error('Error al obtener los registros de la tabla couple:', error);
      res.status(500).json({ error: 'Error al obtener los registros de la tabla couple' });
    }
  });

// Ruta para insertar información en la base de datos
app.post('/registrar', (req, res) => {
  const { partner1name, partner2name } = req.body;

  // Ejecutar la consulta SQL para insertar la información en la base de datos
  pool.query(
    'INSERT INTO couple (partner1name, partner2name) VALUES ($1, $2)',
    [partner1name, partner2name],
    (error, results) => {
      if (error) {
        console.error('Error al insertar en la base de datos:', error);
        res.status(500).json({ message: 'Error al insertar en la base de datos' });
      } else {
        console.log('Información insertada correctamente');
        res.status(200).json({ message: 'Información insertada correctamente' });
      }
    }
  ); 
});

// Ruta para actualizar una pareja existente
app.put('/actualizar/:cod_couple', async (req, res) => {
    try {
      const { cod_couple } = req.params;
      const updatedCoupleData = req.body;
  
      // Actualiza la pareja en la base de datos
      const updateQuery = `
        UPDATE couple
        SET partner1name = $2, partner2name = $3
        WHERE cod_couple = $1
      `;
      
      const { partner1name, partner2name } = updatedCoupleData;
      await pool.query(updateQuery, [cod_couple, partner1name, partner2name]);
  
      res.status(200).json({ message: 'Pareja actualizada con éxito' });
    } catch (error) {
      console.error('Error al actualizar la pareja:', error);
      res.status(500).json({ error: 'Error al actualizar la pareja' });
    }
  });
  

// Ruta para eliminar un registro por su ID
app.delete('/eliminar/:cod_couple', async (req, res) => {
    const { cod_couple } = req.params;
  
    try {
      // Ejecuta la consulta DELETE en la base de datos
      const query = 'DELETE FROM couple WHERE cod_couple = $1';
      const result = await pool.query(query, [cod_couple]);
  
      // Verifica si se eliminó algún registro
      if (result.rowCount === 1) {
        res.status(200).json({ message: 'Registro eliminado correctamente' });
      } else {
        res.status(404).json({ message: 'Registro no encontrado' });
      }
    } catch (error) {
      console.error('Error al eliminar el registro:', error);
      res.status(500).json({ message: 'Error interno del servidor' });
    }
  });

app.listen(port, () => {
  console.log(`Servidor en ejecución en http://localhost:${port}`);
});
