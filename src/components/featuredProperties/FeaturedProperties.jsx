import { useEffect, useState } from "react";
import "./featuredProperties.css";
import axios from "axios";

const FeaturedProperties = () => {

  const [vipRooms, setVipRooms] = useState([]);

  useEffect(() => {
    axios.get("http://localhost:8080/api/getTop4VipRoom").then(res => {
      setVipRooms(res.data)
    })
  }, [])
  
  return (
    <div className="fp">

      {vipRooms.map((room) => (
        <div className="fpItem">
          <img
            src={'http://localhost:8080/images/' + room.imageFile}
            alt=""
            className="fpImg"
          />
          <span className="fpName">{room.name}</span>
          <span className="fpCity">{room.roomType.name}</span>
          <span className="fpPrice">{room.price}</span>
          <div className="fpRating">
            <button>8.9</button>
            <span>Excellent</span>
          </div>
        </div>
      ))}

      {/* <div className="fpItem">
        <img
          src="https://cf.bstatic.com/xdata/images/hotel/max1280x900/215955381.jpg?k=ff739d1d9e0c8e233f78ee3ced82743ef0355e925df8db7135d83b55a00ca07a&o=&hp=1"
          alt=""
          className="fpImg"
        />
        <span className="fpName">Comfort Suites Airport</span>
        <span className="fpCity">Austin</span>
        <span className="fpPrice">Starting from $140</span>
        <div className="fpRating">
          <button>9.3</button>
          <span>Exceptional</span>
        </div>
      </div>
      <div className="fpItem">
        <img
          src="https://cf.bstatic.com/xdata/images/city/max500/957801.webp?k=a969e39bcd40cdcc21786ba92826063e3cb09bf307bcfeac2aa392b838e9b7a5&o="
          alt=""
          className="fpImg"
        />
        <span className="fpName">Four Seasons Hotel</span>
        <span className="fpCity">Lisbon</span>
        <span className="fpPrice">Starting from $99</span>
        <div className="fpRating">
          <button>8.8</button>
          <span>Excellent</span>
        </div>
      </div>
      <div className="fpItem">
        <img
          src="https://cf.bstatic.com/xdata/images/city/max500/957801.webp?k=a969e39bcd40cdcc21786ba92826063e3cb09bf307bcfeac2aa392b838e9b7a5&o="
          alt=""
          className="fpImg"
        />
        <span className="fpName">Hilton Garden Inn</span>
        <span className="fpCity">Berlin</span>
        <span className="fpPrice">Starting from $105</span>
        <div className="fpRating">
          <button>8.9</button>
          <span>Excellent</span>
        </div>
      </div> */}
    </div>
  );
};

export default FeaturedProperties;