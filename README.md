# RDAP client library for Java

This is the (to my knowledge) only comprehensive Java client library for RDAP, the successor of Whois.

Right now, this library is still in development. It is usable and should be production ready, but frequent changes will still be made.

## Installation
The library is available on Maven Central and can easily be added to Maven projects using the following dependency block:
```
<dependency>
    <groupId>cc.maria.rdap-java</groupId>
    <artifactId>rdap-java</artifactId>
    <version>1.0.0</version>
</dependency>
```

If your project does not use a dependency manager, you can download pre-built files from the repository at https://releases.maria.dev/rdap-java. You will usually want to use the latest non-snapshot version. You can find additional information in the [Builds and Javadocs](#builds-and-javadocs) section.

## Usage
Basic usage of the library is very simple:

```java
import cc.maria.rdap.RDAPClient;
import cc.maria.rdap.object.DomainObjectClass;
import cc.maria.rdap.object.ObjectReference;
import cc.maria.rdap.object.ObjectType;

public static void queryDomain () {
    DomainObjectClass domain = new RDAPClient().queryDomain(new ObjectReference("maria.cc", ObjectType.DOMAIN));
    System.out.println(domain.getNameservers()[0].getLdhName());
}
```

Types other than domains can be implicitly detected:

```java
import cc.maria.rdap.RDAPClient;
import cc.maria.rdap.object.IPNetworkObjectClass;

public static void queryIP() {
    IPNetworkObjectClass ipNetwork = new RDAPClient().queryIPNetwork(new ObjectReference("2.56.11.1"));
    System.out.println(ipNetwork.getName());
}
```

You can also explicitly specify the RDAP server to query:

```java
import cc.maria.rdap.RDAPClient;
import cc.maria.rdap.object.DomainObjectClass;
import cc.maria.rdap.object.ObjectReference;

public static void queryDomainWithSpecificServiceURL() {
    DomainObjectClass domain = new RDAPClient("https://rdap.markmonitor.com/rdap/").queryDomain(new ObjectReference("google.com", ObjectType.DOMAIN));
    System.out.println(domain.getRegistrantContact().getvCard().getOrganization().getValues().get(0));
}
```

## Bootstrapping and Redirectors
This library supports bootstrapping via the IANA bootstrap registries as defined in RFC 9224, but a redirector (as defined in RFC 7480 Appendix C) can be used by setting it as the service URL when creating an RDAPClient instance.

## Compatibility
This client library implements the following standards:
- [RFC 7480 (RDAP over HTTP)](https://datatracker.ietf.org/doc/html/rfc7480)
- [RFC 8056 (RDAP-EPP status mapping)](https://datatracker.ietf.org/doc/html/rfc8056)
- [RFC 8521 (RDAP object tagging)](https://datatracker.ietf.org/doc/html/rfc8521)
- [RFC 9082 (RDAP query format)](https://datatracker.ietf.org/doc/html/rfc9082)
- [RFC 9083 (RDAP JSON responses)](https://datatracker.ietf.org/doc/html/rfc9083)
- [RFC 9224 (RDAP server discovery)](https://datatracker.ietf.org/doc/html/rfc9224)

## Handle Correction and Type Detection
The library will automatically make the following corrections to handles:
- Remove any mention of AS or ASN for references of type ASN (or where the type is assumed to be ASN, so where the handle is prefixed with AS or ASN but the type is not set)

The library will further attempt to detect object types by the following criteria if no object type is set:
- ASN: Any string consisting of the prefix AS or ASN followed by only digits
- ASN: Any number
- IPv4: Four sets of one to three digits separated by dots, optionally ending in a slash followed by a two-digit number
- IPv6: Valid IPv6 address or subnet as determined by the [IPAddress library](https://github.com/seancfoley/IPAddress)

Auto-detection rules are evaluated in the order they are listed above and the first match is applied.

Auto-detection will not occur if a type is set in the object reference. Handle corrections cannot be disabled, but if they cause issues (with the correct type set), this is a bug and should be reported accordingly. Handle corrections are ran after auto-detection and are thus applied to auto-detected types as well.

## Known Limitations
- The library does not implement retries in case of rate limiting (RFC 7480 5.5) because correct handling will depend on the application
- The library does not distinguish between object classes and response bodies. Therefore, it allows RDAP Conformance and Notices objects on any object class, instead of just the top level response itself as specified by RFC 9083.
- The library does not implement [RFC 8605 (RDAP vCard extensions)](https://datatracker.ietf.org/doc/html/rfc8605). The properties specified there can be extracted from the VCard object as raw properties. Helper functionality for this might be added later on.
- RFC 9083 does not specify the date time format to be used in events. An implementation that parses all known used formats is planned, right now the field is simply returned as a String by the library.

## Planned Additions
- Support for commonly used extensions (see https://www.iana.org/assignments/rdap-extensions/rdap-extensions.xhtml)
- Detection of excessive caches and circumvention of those (see RFC 7480 Appendix B)
- Merging responses from multiple responsible servers (e.g. registrar and registry)

## Builds and Javadocs
Build outputs for each version can be found at https://releases.maria.dev/rdap-java/. Javadocs for each version can be found at https://javadocs.maria.dev/rdap-java/. Build outputs and Javadocs for snapshots will be overwritten every time a new version of the snapshot is built.

The CI is available publicly at https://teamcity.maria.dev/project/RdapJava?guest=1. Build outputs for all snapshot builds can be found there.

## Changelog
### 1.0.1
- Fixed custom service URLs
- Added application/json as an accepted response type as MarkMonitor will refuse to respond to application/rdap+json only

### 1.0.0
- Initial Release