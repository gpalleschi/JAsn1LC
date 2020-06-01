# JAsn1LC

## Description

**A JAVA ASN1 BER (Basic Encoding Rules) encoder/decoder. This tool can decode/encode any type of ASN1 BER File.**

Use: `java -jar JAsn1LC.jar <File Asn1/Text>` `[-s<File Name Conversion>]` `[-h]` `[-o]` `[-t]` `[-npv]` `[-lt]` `[-ll]` `[-nl]` `[-ni]` `[-b]` `[-e]` `[-c<File Name Asn1 Output>]` `[-help]`

Parameters `[...]` are optional
Parameters `<...>` are mandatory

**`<File Asn1/Text>`**           : Input File Asn1 **_(DECODE MODE)_** or Text **_(ENCODE)_**  
**`[-s<File Name Conversion>]`** : Conversion File. **_(DECODE MODE)_**  
**`[-h]`**                       : Display Hexadecimal Value for Tags **_(DECODE MODE)_**  
**`[-o]`**                       : Display Offset for each Tag with relative level **_(DECODE MODE)_**  
**`[-t]`**                       : Display Only value of Tag Class instead of Id-Tag Class (To use for TAP rappresentation) **_(DECODE MODE)_**  
**`[-npv]`**                     : No Display primitive Values **_(DECODE MODE)_**  
**`[-nl]`**                      : No Display Length for Tags **_(DECODE MODE)_**  
**`[-ni]`**                      : No Tag Indentation **_(DECODE MODE)_**  
**`[-b]`**                       : Specify Byte From **_(DECODE MODE)_**  
**`[-e]`**                       : Specify Byte To  **_(DECODE MODE)_**  
**`[-c<File Name Asn1 Output>]`**: Asn1 File Output **_(ENCODE MODE)_**  
**`[-help]`**                    : Show help informations **_(ENCODE/DECODE MODE)_**  

## Decode Mode

This modality permits to show in standard output an ASN1 BER File.  
Here an example of output :

**java -jar JAsn1LC ./CDARGTMARGTP02595.encoded -o -t**    

* * *
`ASN1 FILE ./CDARGTMARGTP02595.encoded SIZE : 501810`  

`00000000:001 [1] {TransferBatch} length : 491534`  
`00000005:002   [1.4] {BatchControlInfo} length : 121`  
`00000007:003     [1.4.196] {Sender} length : 5  "415247544d"h Value (ARGTM)A`  
`00000016:003     [1.4.182] {Recipient} length : 5  "4152475450"h Value (ARGTP)A`  
`00000025:003     [1.4.109] {FileSequenceNumber} length : 5  "3032353935"h Value (02595)A`  
`00000033:003     [1.4.108] {FileCreationTimeStamp} length : 25`  
`00000036:004         [1.4.108.16] {LocalTimeStamp} length : 14  "3230313831323238303730313134"h Value (20181228070114)A`  
`00000052:004         [1.4.108.231] {UtcTimeOffset} length : 5  "2d30333030"h Value (-0300)A`  
`00000061:003     [1.4.227] {TransferCutOffTimeStamp} length : 25`  
`00000065:004         [1.4.227.16] {LocalTimeStamp} length : 14  "3230313831323238303730313134"h Value   (20181228070114)A`  
* * *

In **DECODE** Modality is possible specify in addition to the input file, a convertion csv file with parameter **-s**.
Each record of this file has this format : **`<Tag Name>`**|**`<Conversion Type>`**|**`<Desc Tag>`** *\{See TAG312 and RAP15 CONV_FILE in project [ASN.1-Reader](https://github.com/gpalleschi/ASN.1-Reader)\}*.

- **`<Tag Name>`**          : In format Id-Class or only Class (**-t** parameter)
- **`<Conversion Type>`**   : Type of conversion

Type|description
---|-----------
 A | Hex to Ascii
 B | Hex to Binary
 N | Hex to Number

- **`<Desc Tag>`**          : Description of Tag to visualize in **_(DECODE MODE)_**

Example Record : **_1-15|N|Total Records_**

Running Examples : 

+ **java -jar JAsn1LC.jar ../TAP312/CDARGTPARGTM00012 -sConv_TAP312 -t -h -o**
+ **java -jar JAsn1LC.jar ../TAP312/CDARGTPARGTM00012 -h -npv**

## Encode Mode

In **ENCODE** Modality is necesary specify in addition to the input file, the parameter **-c** with the output file Asn1 to create, if are presents others parameters will be ingnored.

In this modality, the input file is important that is in ascii format producer for previous **DECODE** run.
It's very important that each record contains Tag, between **`[...]`** and Hexadecimal Value between **"..."** (if Tag is primitve). If in a record is present string **indefinite length**, Tag will be generated with infinitive length.
It's very important that Tag respect hierarchical structure where each level is separated by character dot **'.'**. Length will be recalculated, if present in input Record the length value it not will be considerated.

Here an example :

`00000000:001` **`[`<span style="color:red">1</span>`]`**` {TransferBatch} length : 10140490`  
`00000005:002` **`[1.`<span style="color:red">4</span>`]`**` {BatchControlInfo} length : 119`  
`00000007:003` **`[1.4.`<span style="color:red">196</span>`]`**` {Sender} length : 5  `**`"415247544d"`**`h Value (ARGTM)A`  
`00000007:003` **`[1.4.`<span style="color:red">101</span>`]`**` {Sender} length : 2  `**`"4152"`**`h Value (AR)A`  
`00000126:002`   **`[1.`<span style="color:red">5</span>`]`**` {AccountingInfo} length : 42`  
`00000128:003`     **`[1.5.`<span style="color:red">211</span>`]`**` {TaxationList} length : 0`  

Running Example : 

+ **java -jar JAsn1LC.jar -c./OUTPUT/CDARGTPARGTM00013.new ../TAP312/CDARGTPARGTM00013.txt**

## Getting Started

Copy jar on your local machine. Run it "**java -jar JAsn1LC.jar .....**"

### Prerequisites

`>= Java 1.7`  

## Built With

* [Eclipse](https://www.eclipse.org/) 

## Authors

* **Giovanni Palleschi** - [gpalleschi](https://github.com/gpalleschi)

## License

This project is licensed under the GNU GENERAL PUBLIC LICENSE 3.0 License - see the [LICENSE](LICENSE) file for details