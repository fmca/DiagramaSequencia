@echo off
rem Runs a collection of VDM++ test examples
rem Assumes specification is in Word RTF files

set S1=Diagrama.rtf
set S2=DiagramaTeste.rtf
set S3=Segmento.rtf
set S4=Mensagem.rtf
set S5=BlocoAlt.rtf
set S6=BlocoLoop.rtf
set S7=Objeto.rtf


"C:\Program Files (x86)\The VDM++ Toolbox Lite v8.3.1\bin\vppde" -p -R vdm.tc %S1% %S2% %S3% %S4% %S5% %S6% %S7%
for /R %%f in (*.arg) do call vdmtest "%%f"
