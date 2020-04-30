# JAsn1LC

A simple JAVA ASN1 BER decoder

Use: PrgAsn1.pl <File Asn1> [-s<File Name Conversion>] [-h] [-o] [-t] [-npv] [-lt] [-ll] [-nl] [-ni] [-b] [-e] [-help]
[...] are optional parameters
[-s<File Name Conversion>] : you can add a Conversion File. Each record has this format <Tag Name>|<Conversion Type>|<Desc Tag>
                             Values for <Conversion Type> : A for Hex to Ascii
                                                            B for Hex to Binary
                                                            N for Hex to Number
                             Example Record : 1.15.43|N|Total Records
[-h]                       : Display Hexadecimal Value for Tags
[-o]                       : Display Offset for each Tag
[-t]                       : Display Only value of Tag instead of Id-Tag (To use for TAP rappresentation)
[-npv]                     : No Display primitive Values
[-lt]                      : Display len Tag in Bytes
[-ll]                      : Display len Len in Bytes
[-nl]                      : No Display Length for Tags
[-ni]                      : No Tag Indentation
[-b]                       : Specify Byte From 
[-e]                       : Specify Byte To 

## Getting Started

Copy jar on your local machine. Run it "java -jar JAsn1LC.jar ....."

### Prerequisites

>= Java 1.7  

## Built With

* [Eclipse](https://www.eclipse.org/) 

## Authors

* **Giovanni Palleschi** - [gpalleschi](https://github.com/gpalleschi)

## License

This project is licensed under the GNU GENERAL PUBLIC LICENSE 3.0 License - see the [LICENSE](LICENSE) file for details
