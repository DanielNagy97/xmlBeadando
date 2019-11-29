<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
xmlns:fn="http://www.w3.org/2005/xpath-functions"
xmlns:e="beadando">
  <xsl:template match="e:lemezkolcsonzo">
    <html>
      <head></head>
      <body>
        <h1>XSL lekérdezések</h1>
        <h2>1.Albumok és előadók</h2>
        <table border="1">
          <tr>
            <th>Album</th>
            <th>Eloado</th>
          </tr>
          <xsl:for-each select="e:album">
            <tr>
              <td>
                <xsl:value-of select="e:cim" />
              </td>
              <xsl:variable name="eloadoref" select="@eloadoref" />
              <xsl:for-each select="../e:eloado">
                <xsl:if test="$eloadoref = @eloadoref">
                  <td>
                    <xsl:value-of select="e:nev" />
                  </td>
                </xsl:if>
              </xsl:for-each>
            </tr>
          </xsl:for-each>
        </table>
        <br/>
        <h2>2.Az LP formátumú példányok adatai</h2>
        <table border="1">
          <tr>
            <th>Album címe</th>
            <th>Megjelenés éve</th>
            <th>Kiadás éve</th>
            <th>Állapot</th>
            <th>Elérhető?</th>
          </tr>
          <xsl:for-each select="e:peldany[e:formatum = 'LP']">
            <tr>
              <xsl:variable name="katalogusszam" select="@katalogusszam" />
              <xsl:for-each select="../e:album">
                <xsl:if test="$katalogusszam = @katalogusszam">
                  <td>
                    <xsl:value-of select="e:cim" />
                  </td>
                  <td>
                    <xsl:value-of select="e:megjeleneseve" />
                  </td>
                </xsl:if>
              </xsl:for-each>
              <td>
                 <xsl:value-of select="e:kiadaseve" />
              </td>
              <td>
                 <xsl:value-of select="e:allapot" />
              </td>

              <xsl:variable name="peldanyref" select="@peldanyref" />

              <xsl:for-each select="../e:kolcsonzes">
                <xsl:if test="$peldanyref = @peldanyref">
                  <xsl:choose>
                    <xsl:when test="e:visszahozva = 'false'">
                      <td>Nem</td>
                    </xsl:when>
                    <xsl:when test="e:visszahozva = 'true'">
                      <td>Igen</td>
                    </xsl:when>
                  </xsl:choose>
                </xsl:if>
              </xsl:for-each>

            </tr>
          </xsl:for-each>
        </table>

<xsl:variable name="currentDate" select="format-date(current-date(), '[Y0001][M01][D01]')"/>


<xsl:variable name="se" select="'2019-05-26'" />


                  <xsl:choose>
                    <xsl:when test="$currentDate &lt; 20201001">
                      Kisebb
                    </xsl:when>
                    <xsl:when test="$currentDate &gt; 20201001">
                      Nagyobb
                    </xsl:when>
                  </xsl:choose>


        <h2>3.A késésben lévő kölcsönzők adatai</h2>
        <table border="1">
          <tr>
            <th>Név</th>
            <th>Telefon</th>
            <th>Album címe</th>
            <th>Katalógusszám</th>
            <th>Határidő</th>
          </tr>
          <xsl:for-each select="e:album">
            <tr>
              <td>
                <xsl:value-of select="e:cim" />
              </td>
              <xsl:variable name="eloadoref" select="@eloadoref" />
              <xsl:for-each select="../e:eloado">
                <xsl:if test="$eloadoref = @eloadoref">
                  <td>
                    <xsl:value-of select="e:nev" />
                  </td>
                </xsl:if>
              </xsl:for-each>
            </tr>
          </xsl:for-each>
        </table>






      </body>
    </html>
  </xsl:template>
</xsl:stylesheet>
