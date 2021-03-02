Clear-Host

Write-Output " "
Write-Output "=== University of Wisconsin - La Crosse ==="
Write-Output "========= Automatic Testing Suite ========="
Write-Output ""
$testDirectory = Read-Host -Prompt 'Input directory to test'

$testCases = Get-ChildItem -Path $testDirectory -Filter *.in

$javaFile = Get-ChildItem -Path $testDirectory -Filter *.java

Write-Output " "
Write-Output "      =+=+= Setup =+=+="
Write-Output ("COMPILING " + $javaFile.Name + "...")

Remove-Item "autoTester.error"

Start-Process "D:\openjdk-15.0.2_windows-x64_bin\jdk-15.0.2\bin\javac.exe" "$testDirectory/$javaFile" -RedirectStandardError "autoTester.error" -NoNewWindow -Wait
Write-Output ("Compilation Successful!")

$error_input = Get-Content ".\autoTester.error"

if ($error_input -ne "")
{
    Write-Output ""
    Write-Output "ERROR WHILE COMPILIING. EXITING."
    Write-Output ""

    exit
}

Write-Output " "
Write-Output "     =+=+= Tests =+=+="
Write-Output ("Running " + $testCases.Length + " test cases...")

ForEach ($testCase in $testCases)
{
    $fileNameWithoutExtension = $javaFile.BaseName

    $stdinRedirection = ("./" + $testDirectory + "/" + $testCase.Name)
    $stdoutRedirection = ("./" + $testDirectory + "/" + $testCase.BaseName + ".out")

    Start-Process "D:\openjdk-15.0.2_windows-x64_bin\jdk-15.0.2\bin\java.exe" "$testDirectory/$fileNameWithoutExtension" -NoNewWindow -RedirectStandardInput $stdinRedirection -RedirectStandardOutput $stdoutRedirection
}


$testResults = Get-ChildItem -Path $testDirectory -Filter *.out

if ($testCases.Length -eq $testResults.Length)
{
    Write-Output "All test(s) ran without error."
}
else
{
    Write-Output ("==WARNING== Only " + $testResults.Length + " of " + $testCases.Length + " ran without error!")
}

Write-Output ""
Write-Output "Checking Test Results..."
Write-Output ""

$bool = 1

foreach ($result in $testResults)
{
    $expected = Get-Content ("./" + $testDirectory + "/" + $result.BaseName + ".ans")
    $actual = Get-Content $result.FullName

    if ($expected -eq $actual)
    {
        Write-Output "OK"
    }
    else
    {
        Write-Output "TEST FAILED!"
        $bool = 0;
    }
}

if ($bool -eq 1)
{
    Write-Output "====== ALL TESTS PASSED ======"
}
else
{
    Write-Output "+++++++++ TEST FAILED ++++++++"
}

Write-Output " "
Write-Output " "