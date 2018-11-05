/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.eltorneo.mvc.dao;

import co.eltorneo.common.util.AsignaAtributoStatement;
import co.eltorneo.common.util.LoggerMessage;
import co.eltorneo.mvc.dto.EquipoDTO;
import co.eltorneo.mvc.dto.PartidoDTO;
import co.eltorneo.mvc.dto.RespuestaDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Administrador
 */
public class PartidoDAO {

    public RespuestaDTO registrarPartido(Connection conexion, PartidoDTO partido) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        int nRows = 0;
        StringBuilder cadSQL = null;
        RespuestaDTO registro = null;

        try {
            registro = new RespuestaDTO();
            System.out.println("partido " + partido.toStringJson());
            cadSQL = new StringBuilder();
            cadSQL.append(" INSERT INTO partido(part_dia, hopa_id,part_jornada,temp_id)");
            cadSQL.append(" VALUES (?, ?, ? ,?) ");

            ps = conexion.prepareStatement(cadSQL.toString(), Statement.RETURN_GENERATED_KEYS);

            AsignaAtributoStatement.setString(1, partido.getFechaPartido(), ps);
            AsignaAtributoStatement.setString(2, partido.getIdHorario(), ps);
            AsignaAtributoStatement.setInt(3, String.valueOf(partido.getJornada()), ps);
            AsignaAtributoStatement.setString(4, partido.getTemporada(), ps);

            nRows = ps.executeUpdate();
            if (nRows > 0) {
                rs = ps.getGeneratedKeys();
                registro.setRegistro(true);
                if (rs.next()) {
                    registro.setIdResgistrado(rs.getString(1));

                }
                rs.close();
                rs = null;
            }
            ps.close();
            ps = null;

        } catch (SQLException se) {
            LoggerMessage.getInstancia().loggerMessageException(se);
            return null;
        }
        return registro;
    }

    /**
     *
     * @param conexion
     * @param partido
     * @return
     * @throws SQLException
     */
    public RespuestaDTO registrarPartidoJuego(Connection conexion, PartidoDTO partido) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        int nRows = 0;
        StringBuilder cadSQL = null;
        RespuestaDTO registro = null;

        try {
            registro = new RespuestaDTO();
            System.out.println("partido " + partido.toStringJson());
            cadSQL = new StringBuilder();
            cadSQL.append(" INSERT INTO partido_equipo(equi_id, part_id)");
            cadSQL.append(" VALUES (?,?) ");

            ps = conexion.prepareStatement(cadSQL.toString(), Statement.RETURN_GENERATED_KEYS);

            AsignaAtributoStatement.setString(1, partido.getEquipoA(), ps);
            AsignaAtributoStatement.setString(2, partido.getId(), ps);

            nRows = ps.executeUpdate();
            if (nRows > 0) {
                rs = ps.getGeneratedKeys();
                registro.setRegistro(true);
                if (rs.next()) {
                    registro.setIdResgistrado(rs.getString(1));

                }
                rs.close();
                rs = null;
            }
            ps.close();
            ps = null;

        } catch (SQLException se) {
            LoggerMessage.getInstancia().loggerMessageException(se);
            return null;
        }
        return registro;
    }

    /**
     *
     * @param conexion
     * @return
     */
    public ArrayList<PartidoDTO> listarPartidos(Connection conexion) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<PartidoDTO> partidos = null;
        PartidoDTO part = null;
        StringBuilder cadSQL = null;

        try {

            cadSQL = new StringBuilder();
            cadSQL.append(" SELECT part_id, part_jornada");
            cadSQL.append(" FROM partido ");
            ps = conexion.prepareStatement(cadSQL.toString());

            rs = ps.executeQuery();
            partidos = new ArrayList();

            while (rs.next()) {
                part = new PartidoDTO();
                part.setId(rs.getString("part_id"));
                part.setJornada(rs.getInt("part_jornada"));
                partidos.add(part);

            }
            ps.close();
            ps = null;

        } catch (Exception e) {
            LoggerMessage.getInstancia().loggerMessageException(e);
            return null;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                    ps = null;
                }
                if (partidos != null && partidos.isEmpty()) {
                    partidos = null;
                }
            } catch (Exception e) {
                LoggerMessage.getInstancia().loggerMessageException(e);
                return null;
            }
        }

        return partidos;
    }

    /**
     * *
     * valida si el equipo ya esta en un partido de la jornada
     *
     * @param conexion
     * @param idEquipo
     * @param jornada
     * @return
     */
    public boolean validarEquipoPorJornada(Connection conexion, String idEquipo, String jornada) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean rta = false;
        StringBuilder cadSQL = null;

        try {

            cadSQL = new StringBuilder();
            cadSQL.append("Select paq.part_id  FROM partido_equipo paq");
            cadSQL.append(" INNER JOIN partido pa ON pa.part_id = paq.part_id");
            cadSQL.append("WHERE paq.equi_id = ? AND pa.part_jornada = ?");
            ps = conexion.prepareStatement(cadSQL.toString());
            AsignaAtributoStatement.setString(1, idEquipo, ps);
            AsignaAtributoStatement.setString(2, jornada, ps);

            rs = ps.executeQuery();

            while (rs.next()) {
                rta = true;

            }
            ps.close();
            ps = null;

        } catch (Exception e) {
            LoggerMessage.getInstancia().loggerMessageException(e);
            return false;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                    ps = null;
                    rs.close();
                    rs = null;
                }
            } catch (Exception e) {
                LoggerMessage.getInstancia().loggerMessageException(e);
                return false;
            }
        }

        return rta;
    }
}
