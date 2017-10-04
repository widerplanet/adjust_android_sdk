#!/usr/bin/ruby

require 'aws-sdk'
require 'net/http'

$PROJECT_ARN = 'arn:aws:devicefarm:us-west-2:645274582855:project:0284e5ea-0b06-4073-9db0-6c35f5b43a57'
$DEVICE_POOL_ARN = 'arn:aws:devicefarm:us-west-2:645274582855:devicepool:7d61ace2-1b2c-4189-8cd9-c3a9ef52eea1/cb79b3c8-739c-49fe-a0fb-79eff72b7f58'
$APK_FILENAME='app.apk'
$TEST_PACKAGE_FILENAME='test_bundle.zip'

def upload_apk(client)
    puts "[*] Acquiring S3 pre_signed APK upload url"
    upload = client.create_upload({
        project_arn: $PROJECT_ARN,
        name: $APK_FILENAME,
        type: "ANDROID_APP",
        content_type: "application/octet-stream"
    })

    app_arn = upload['upload']['arn']
    pre_signed_url = upload['upload']['url']

    puts "app arn #{app_arn} \n"
    puts "pre_signed url: #{pre_signed_url} \n"

    puts
    puts "[*] Attempting to upload APK"
    url = URI.parse(pre_signed_url)
    apk_contents = File.open($APK_FILENAME, "rb").read

    Net::HTTP.start(url.host) do |http|
        http.send_request("PUT", url.request_uri, apk_contents, {"content-type" => "application/octet-stream"})
    end

    loop do
        puts "[*] Uploading..."
        response = client.get_upload({
            arn: app_arn
        })

        break if response['upload']['status'] != "SUCCESS"
        sleep(1)
    end

    puts "[*] APK uploaded"
    puts
    return app_arn
end

def upload_test_package(client)
    puts "[*] Acquiring S3 pre_signed Appium test package upload url"
    upload = client.create_upload({
        project_arn: $PROJECT_ARN,
        name: $TEST_PACKAGE_FILENAME,
        type: "APPIUM_JAVA_TESTNG_TEST_PACKAGE"
    })

    test_package_arn = upload['upload']['arn']
    pre_signed_url = upload['upload']['url']

    puts "test package arn #{test_package_arn} \n"
    puts "pre_signed url: #{pre_signed_url} \n"

    puts
    puts "[*] Attempting to upload Appium test package"
    url = URI.parse(pre_signed_url)
    apk_contents = File.open($TEST_PACKAGE_FILENAME, "rb").read

    Net::HTTP.start(url.host) do |http|
        http.send_request("PUT", url.request_uri, apk_contents)
    end

    loop do
        puts "[*] Uploading..."
        response = client.get_upload({
            arn: test_package_arn
        })

        break if response['upload']['status'] != "SUCCESS"
        sleep(1)
    end

    puts "[*] Appium test Uploaded"
    puts
    return test_package_arn
end


def schedule_run(client, app_arn, test_script_arn)
    puts "[*] Scheduling test run"

    response = client.schedule_run({
        name: "ruby_test_run",
        device_pool_arn: $DEVICE_POOL_ARN,
        project_arn: $PROJECT_ARN,
        app_arn: app_arn,
        test: {
            type: "APPIUM_JAVA_TESTNG",
            test_package_arn: test_script_arn
        }
    })
end

puts "[*] START"
puts

df_client = Aws::DeviceFarm::Client.new()

app_arn = upload_apk(df_client)
test_package_arn = upload_test_package(df_client)

schedule_run(df_client, app_arn, test_package_arn)

puts "[*] END"
puts
