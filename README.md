# ODM Spec

[![Build Status](https://travis-ci.org/alexanderkiel/odm-spec.svg?branch=master)](https://travis-ci.org/alexanderkiel/odm-spec)

Clojure specs for [CDISC ODM][1] data structures.

## Usage

Add the following dependency to your project.

```clojure
[org.clojars.akiel/odm-spec "0.3-alpha14"]
```

Require `[odm.file]` in your namespace.

## Currently Omitted Entities and Attributes
 
 * everything with SAS
 * 3.1.1.3.7.2 - ExternalCodeList
 * 3.1.1.3.6.2 - ExternalQuestion
 * 3.1.1.3.6.4 - RangeCheck
 * 3.1.1.3.8 - ArchiveLayout
 * 3.1.1.3.10 - Presentation
 * 3.1.4.1.1.1.1.1   ItemData
 * 3.1.4.1.4 - Annotation
 * 3.1.5 - Association
 * 3.1.2 - AdminData
 * 3.1.4.1.2 - AuditRecord
 * 3.1.4.1.3 - Signature
 * 3.1.4.1.4 - Annotation
 * 3.1.4.1.5 - InvestigatorRef
 * 3.1.4.1.6 - SiteRef
 * 4 - Digital Signatures
 

## License

Copyright © 2016 Alexander Kiel

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

[1]: <http://www.cdisc.org/odm>
