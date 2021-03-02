#TODO: Show the tests that fail.
#TODO: Run it periodically or even better, on save!
#TODO: Time runs

Clear-Host

Write-Output ""
Write-Output "==========================================="
Write-Host "=== University of Wisconsin - La Crosse ===" -ForegroundColor Yellow
Write-Host "========= Automatic Testing Suite =========" -ForegroundColor Yellow
Write-Output "==========================================="
Write-Output ""

$testDirectory = Read-Host -Prompt 'Input directory to test'

$testCases = Get-ChildItem -Path $testDirectory -Filter *.in

$javaFile = Get-ChildItem -Path $testDirectory -Filter *.java

Write-Output " "
Write-Host "====== Setup ======" -ForegroundColor Cyan
Write-Output ("COMPILING " + $javaFile.Name + "...")

Remove-Item "autoTester.error"

Start-Process "D:\openjdk-15.0.2_windows-x64_bin\jdk-15.0.2\bin\javac.exe" "$testDirectory/$javaFile" -RedirectStandardError "autoTester.error" -NoNewWindow -Wait
$error_input = Get-Content ".\autoTester.error"

if ($error_input)
{
    Write-Output ""
    Write-Host "ERROR WHILE COMPILIING. EXITING." -ForegroundColor Yellow
    Write-Output ""
    $error_input | Out-String | Write-Host -ForegroundColor Red
    Write-Output ""

    exit
}
else
{
    Write-Output ("Compilation Successful!")
}

Write-Output " "
Write-Host "====== Tests ======" -ForegroundColor Cyan
Write-Output ("Running " + $testCases.Length + " test cases...")

ForEach ($testCase in $testCases)
{
    $fileNameWithoutExtension = $javaFile.BaseName

    $stdinRedirection = ("./" + $testDirectory + "/" + $testCase.Name)
    $stdoutRedirection = ("./" + $testDirectory + "/" + $testCase.BaseName + ".out")

    Start-Process "D:\openjdk-15.0.2_windows-x64_bin\jdk-15.0.2\bin\java.exe" "$testDirectory/$fileNameWithoutExtension" -NoNewWindow -Wait -RedirectStandardInput $stdinRedirection -RedirectStandardOutput $stdoutRedirection
}


$testResults = Get-ChildItem -Path $testDirectory -Filter *.out

if ($testCases.Length -eq $testResults.Length)
{
    Write-Output "All test(s) ran without error."
}
else
{
    Write-Host ("==WARNING== Only " + $testResults.Length + " of " + $testCases.Length + " ran without error!") -ForegroundColor Yellow
}

Write-Output ""
Write-Output "Checking Test Results..."

$bool = 1

foreach ($result in $testResults)
{
    $expected = (Get-Content ("./" + $testDirectory + "/" + $result.BaseName + ".ans"))
    $actual = (Get-Content ("./" + $testDirectory + "/" + $result.Name))

    if (($expected).GetHashCode() -eq ($actual).GetHashCode())
    {
        Write-Output "OK"

    }
    else
    {
        Write-Host "TEST FAILED!" -BackgroundColor Red
        $bool = 0;
    }
}

Write-Output ""

if ($bool -eq 1)
{
    Write-Host "====== ALL TESTS PASSED ======" -ForegroundColor Green
}
else
{
    Write-Host "+++++++++ TEST FAILED ++++++++" -ForegroundColor Red
    Write-Output " "
    Write-Host "Run 'Compare-Object (Get-Content $( $testDirectory )/*.ans) (Get-Content $( $testDirectory )/*.out)' for more information."
}

Write-Output " "
Write-Output " "

