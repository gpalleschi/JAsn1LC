JAsn1LC  

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