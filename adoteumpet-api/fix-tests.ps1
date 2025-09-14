# Script para corrigir os testes que usam $.errors para $.details
$filePath = "src\test\java\com\adoteumpet\adoteumpetapi\controller\PetControllerPostTest.java"

# Ler o conteúdo do arquivo
$content = Get-Content $filePath -Raw

# Substituir todas as ocorrências de $.errors por $.details
$newContent = $content -replace '"\$\.errors"\)', '"$.details")'

# Escrever o conteúdo de volta para o arquivo
$newContent | Set-Content $filePath -NoNewline -Encoding UTF8

Write-Host "Arquivo corrigido: $filePath"
Write-Host "Substituições feitas: $.errors -> $.details"