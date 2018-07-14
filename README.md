# hb-test-task

Test task for HalalBooking company.

If you want an interactive development environment run:
```
lein figwheel
```
and open your browser at [localhost:3449](http://localhost:3449/).

If, for some reason, you want a production build run:
```
lein do clean, cljsbuild once min
```
and open your browser in `resources/public/index.html`.
