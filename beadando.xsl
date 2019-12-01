<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="2.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:fn="http://www.w3.org/2005/xpath-functions"
    xmlns:e="beadando">
    <xsl:template match="e:lemezkolcsonzo">
        <html>
            <head></head>
            <body>
                <h1>XSL lekérdezések</h1>
                <h2>1.A Mint és Near Mint állapotú pédányok adatai</h2>
                <table border="1">
                    <tr>
                        <th>Album címe</th>
                        <th>Előadó</th>
                        <th>Műfaj</th>
                        <th>Állapot</th>
                        <th>Formátum</th>
                    </tr>
                    <xsl:for-each select="e:peldany[e:allapot = 'Mint' or e:allapot = 'Near Mint']">
                        <tr>
                            <xsl:variable name="katalogusszam" select="@katalogusszam" />
                            <xsl:variable name="album" select="../e:album[$katalogusszam = @katalogusszam]" />
                            <td>
                                <xsl:value-of select="$album/e:cim" />
                            </td>
                            <td>
                                <xsl:value-of select="../e:eloado[$album/@eloadoref = @eloadoref]/e:nev" />
                            </td>
                            <td>
                                <xsl:value-of select="$album/e:mufaj" />
                            </td>
                            <td>
                                <xsl:value-of select="e:allapot" />
                            </td>
                            <td>
                                <xsl:value-of select="e:formatum" />
                            </td>
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
                            <xsl:variable name="kolcsonozve" select="(../e:kolcsonzes[@peldanyref = $peldanyref])[last()]/e:visszahozva" />
                            <xsl:if test="boolean($kolcsonozve)">
                                <xsl:choose>
                                    <xsl:when test="$kolcsonozve = 'false'">
                                        <td>Nem</td>
                                    </xsl:when>
                                    <xsl:when test="$kolcsonozve = 'true'">
                                        <td>Igen</td>
                                    </xsl:when>
                                </xsl:choose>
                            </xsl:if>
                            <xsl:if test="boolean($kolcsonozve) = false()">
                                <td>Igen</td>
                            </xsl:if>
                        </tr>
                    </xsl:for-each>
                </table>
                <br/>
                <xsl:variable name="currentDate" select="current-date()"/>
                <xsl:variable name="currentFormattedDate" select="format-date($currentDate, '[Y0001][M01][D01]')"/>
                <h2>3.A késésben lévő kölcsönzők adatai</h2>
                <h3>A mai dátum: <xsl:value-of select="$currentDate" /></h3>
                <table border="1">
                    <tr>
                        <th>Név</th>
                        <th>Telefon</th>
                        <th>Lakcím</th>
                        <th>Album címe</th>
                        <th>Album formátuma</th>
                        <th>Határidő</th>
                    </tr>
                    <xsl:for-each select="e:kolcsonzes[concat(concat(substring(e:meddig,1,4),substring(e:meddig,6,2)),substring(e:meddig,9,2)) &lt; $currentFormattedDate and e:visszahozva = 'false']">
                        <tr>
                            <xsl:variable name="kolcsonzoref" select="@kolcsonzoref" />
                            <xsl:for-each select="../e:kolcsonzo">
                                <xsl:if test="$kolcsonzoref = @kolcsonzoref">
                                    <td>
                                        <xsl:value-of select="e:nev" />
                                    </td>
                                    <td>
                                        <xsl:value-of select="e:telefon" />
                                    </td>
                                    <td>
                                        <xsl:value-of select="e:lakcim" />
                                    </td>
                                </xsl:if>
                            </xsl:for-each>
                            <xsl:variable name="peldanyref" select="@peldanyref" />
                            <xsl:for-each select="../e:peldany">
                                <xsl:if test="$peldanyref = @peldanyref">
                                    <xsl:variable name="katalogusszam" select="@katalogusszam" />
                                    <xsl:for-each select="../e:album">
                                        <xsl:if test="$katalogusszam = @katalogusszam">
                                            <td>
                                                <xsl:value-of select="e:cim" />
                                            </td>
                                        </xsl:if>
                                    </xsl:for-each>
                                    <td>
                                        <xsl:value-of select="e:formatum" />
                                    </td>
                                </xsl:if>
                            </xsl:for-each>
                            <td>
                                <xsl:value-of select="e:meddig" />
                            </td>
                        </tr>
                    </xsl:for-each>
                </table>    
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>