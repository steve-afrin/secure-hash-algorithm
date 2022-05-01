# [Secure Hash Algorithms (SHAs)](https://cryptobook.nakov.com/cryptographic-hash-functions/secure-hash-algorithms)

For years we've seen CRCs and checksums of files on the Internet as a means of verifying that you successfully
downloaded or received a _correct_ or _valid_ copy of a file. By the term _correct_ or _valid_ I mean that the
file is verified on the receiving end as a file that has not been tampered with or altered by a bad actor somewhere
in the transfer process.

But how exactly are those CRCs or checksums calculated? How do they help to verify that a file has not been tampered
with during transfer, and more importantly, how secure are those means of validation for unaltered files?

This project was originally designed to help me better understand what secure hash algorithms are and how message
digests are properly calculated in Java. Hopefully this information may be beneficial to other developers as well.

## Some Basics

Secure hash algorithms are generally one-way manipulations of data with the intention to generate a hash value that
uniquely identifies the data or file contents. _Collisions_ of hash values are considered very bad and insecure and
make the hash algorithm fundamentally unsafe for validating uniqueness of data.

The most common means of generating secure checksums in 2022 uses the
[Merkle–Damgård construction](https://en.wikipedia.org/wiki/Merkle%E2%80%93Damg%C3%A5rd_construction),
but there are other means for calculating secure checksums in a faster, more efficient, and more secure manner.

Part of how secure a hash algorithm is involves assessing how likely the algorithm avoids collisions from generating
the same hash values. Very good hash algorithms have an infitesimally small (nearly non-existent) chance of generating
the same hash value for two separate sets of data.

Suppose you have two different files: File A and File B. If File A and File B can generate the same hash value, this
winds up being very bad because you truly have no way to use the hash value to uniquely identify File A as being
different (a.k.a. having different content) from File B. The only way the two files should generate the same hash
value is if they are bit-for-bit equivalent in content.

If even one bit is different between the files, then the fact that the _same_ hash value is generated inherently makes
the hash algorithm insecure and useless for purposes of identifying uniqueness across different data sets.

## SHA-1

_Much more elaboration to be done here and in the `SHA-2` and `SHA-3` sections as well._

### [MD5](https://en.wikipedia.org/wiki/MD5) (1992)

* Originally designed to be used as a cryptographic hash function

* Size of the hash value is 128 bits (16 bytes)

* Unsecure (2008) because collision space is comparatively small

    * A collision attack exists that can find collisions within seconds on a computer with a 2.6 GHz Pentium 4
      processor (complexity of 2^{24.1}).

    * There is also a chosen-prefix collision attack that can produce a collision for two inputs with specified
      prefixes within seconds, using off-the-shelf computing hardware (complexity of 2^{39}).

## SHA-2 (2001)

### [SHA-256](https://www.n-able.com/blog/sha-256-encryption)

* Size of the hash value is 256 bits (32 bytes)

* Collision attack is complexity of 2^{256}

### [SHA-512](https://komodoplatform.com/en/academy/sha-512/)

* Size of the hash value is 512 bits (64 bytes)

## SHA-3 (2015)

* Subset of the broader cryptographic primitive family **Keccak**
* Keccak is based on a novel approach called sponge construction.

### SHA3-256

### SHA3-512